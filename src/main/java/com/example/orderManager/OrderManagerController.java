package com.example.orderManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderManagerController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    StockMovementRepository stockMovementRepository;

    @Autowired
    OrderRequestRepository orderRequestRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    LogsRepository logsRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("stockList", stockMovementRepository.findByVisible(true));
        model.addAttribute("customer", new Customer());
        return "index";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        Iterable<StockMovement> stock = stockMovementRepository.findAll();

        model.addAttribute("stockList", stock);
        model.addAttribute("stock", stock.iterator().next());
        sendLogToModel(model);
        return "admin";
    }

    @PostMapping("/updateStock/{id}")
    public String updateStock(@PathVariable("id") long id, StockMovement stock, Model model) {
        StockMovement stockMovement = stockMovementRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));;
        stockMovement.getItem().setName(stock.getItem().getName());
        stockMovement.getItem().setUrlImagem(stock.getItem().getUrlImagem());
        stockMovement.setQuantity(stock.getQuantity());
        stockMovement.setVisible(stock.getVisible());
        stockMovement = stockMovementRepository.save(stockMovement);

        createLog("save stock",
                "Stock id: " + stockMovement.getId() +
                        " nome: " +stockMovement.getItem().getName()+
                        " qtde: " + stockMovement.getQuantity());

        verifyIncompleteOrders(stockMovement);

        model.addAttribute("stockList", stockMovementRepository.findAll());
        model.addAttribute("stock", stockMovement);
        sendLogToModel(model);

        return "admin";
    }

    private void verifyIncompleteOrders(StockMovement stockMovement) {
     List<OrderRequest> orders = orderRequestRepository.findByComplete(false);
     if(!orders.isEmpty()) {
         for(OrderRequest order : orders) {
             if(stockMovement.getId() == order.getStockMovement().getId()) {
                 if(stockMovement.getQuantity() > 0) {
                     order.setQuantity(1);
                     order.setComplete(true);

                     orderRequestRepository.save(order);

                     stockMovement.setQuantity(stockMovement.getQuantity() -1);

                     stockMovementRepository.save(stockMovement);

                     createLog("complete order", "Order id: " + order.getId() + " item: "+
                             order.getStockMovement().getItem().getName() + " cliente: " +
                             order.getCustomer().getName());

                     createLog("send email", "Order id: " + order.getId() + " item: "+
                             order.getStockMovement().getItem().getName() + " cliente: " +
                             order.getCustomer().getName() + " email: " + order.getCustomer().getEmail());

                     createLog("update auto stock",
                             "Stock id: " + stockMovement.getId() +
                                     " nome: " +stockMovement.getItem().getName()+
                                     " qtde: " + stockMovement.getQuantity());
                 }

             }
         }
     }
    }

    @GetMapping("/deleteStock/{id}")
    public String deleteStock(@PathVariable("id") long id, Model model) {

       Optional<StockMovement> stockMovement = stockMovementRepository.findById(id);
        if(stockMovement != null ) {
            List<OrderRequest> orders = orderRequestRepository.findByStockMovement(stockMovement.get());

            if(orders.isEmpty()){
                stockMovementRepository.deleteById(id);
                createLog("delete stock", "Stock id: " + id);
            } else {
                stockMovement.get().setVisible(false);
                stockMovementRepository.save(stockMovement.get());
                createLog("disable stock", "Stock id: " + id);
            }
        }

        if(stockMovementRepository.findAll().iterator().hasNext()){
            Iterable<StockMovement> stock = stockMovementRepository.findAll();
            model.addAttribute("stockList", stock);
            model.addAttribute("stock", stock.iterator().next());
        } else {
            addStock(model);
        }

        sendLogToModel(model);
        return "admin";
    }

    @GetMapping("/addStock")
    public String addStock(Model model) {
        Item item = itemRepository.save(new Item("", ""));

        StockMovement stockMovement = stockMovementRepository.save(new StockMovement( item, 0, true));

        model.addAttribute("stockList", stockMovementRepository.findAll());
        model.addAttribute("stock", stockMovement);
        sendLogToModel(model);
        return "admin";
    }

    @GetMapping("/editStock/{id}")
    public String editStock(@PathVariable("id") long id, Model model) {
        StockMovement stock = stockMovementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

        model.addAttribute("stockList", stockMovementRepository.findAll());
        model.addAttribute("stock", stock);
        sendLogToModel(model);

        return "admin";
    }

    @GetMapping("/message")
    public String message(Model model){
        model.addAttribute("message", "Enviaremos um email para");
        model.addAttribute("messageTitle", "Obrigado por adotar!");

        return "message";
    }

    @PostMapping("/adopt/{id}")
    public String adopt(@PathVariable("id") long id, Customer customer, Model model) {
        StockMovement stock = stockMovementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

        if(!customer.getName().isEmpty() && !customer.getEmail().isEmpty()){
            customer = customerRepository.save(customer);

            if(stock.getQuantity() > 0) {
                OrderRequest orderRequest = new OrderRequest(stock, 1, customer,true );
                orderRequest = orderRequestRepository.save(orderRequest);
                stock.setQuantity(stock.getQuantity() -1);
                stockMovementRepository.save(stock);

                model.addAttribute("message", "Enviaremos um email para " + customer.getEmail() + " para dar continuidade no processo.");
                model.addAttribute("messageTitle", "Obrigado por adotar!");

                createLog("create order", "Order id: " + orderRequest.getId() + " item: "+
                        orderRequest.getStockMovement().getItem().getName() + " cliente: " +
                        orderRequest.getCustomer().getName());
            } else {
                OrderRequest orderRequest = new OrderRequest(stock, 0, customer,false );

                orderRequest = orderRequestRepository.save(orderRequest);

                model.addAttribute("message", "Assim que estiver disponivel para adoção enviaremos um email para " + customer.getEmail() + " para dar continuidade no processo.");
                model.addAttribute("messageTitle", "Obrigado por adotar!");

                createLog("pendent order", "Order id: " + orderRequest.getId() + " item: "+
                        orderRequest.getStockMovement().getItem().getName() + " cliente: " +
                        orderRequest.getCustomer().getName() +
                        " email: " + orderRequest.getCustomer().getEmail() );
            }
            return "message";
        }
        return this.index(model);
    }

    private void createLog(String action, String message) {
        logsRepository.save(new Logs(action, message));
    }

    private void sendLogToModel(Model model){
        Iterable<Logs> logs = logsRepository.findAll();
        model.addAttribute("logs", logs);
    }

}
