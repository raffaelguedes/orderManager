package com.example.orderManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OrderManagerApplication {
	private static final Logger log = LoggerFactory.getLogger(OrderManagerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(OrderManagerApplication.class, args);
	}

	@Bean
	public CommandLineRunner start(ItemRepository itemRepository, StockMovementRepository stockMovementRepository, LogsRepository logsRepository) {
		return (args) -> {

			Item item = itemRepository.save(new Item("Fofinho", "https://i.pinimg.com/originals/06/b2/9f/06b29fe91e8465ee1a35718c0ddf7c91.jpg"));

			stockMovementRepository.save(new StockMovement( item, 1, true));

			logsRepository.save(new Logs("save stock inicial", "Nome: Fofinho Qtde: 1"));

			item = itemRepository.save(new Item("Lindo", "https://s.abcnews.com/images/Lifestyle/CN_worlds_uggliest_dogs_mar_14061720060322_0000_8x9_1600.jpg"));

			stockMovementRepository.save(new StockMovement( item, 0, true));

			logsRepository.save(new Logs("save stock inicial", "Nome: Lindo Qtde: 1"));

			item = itemRepository.save(new Item("Ninhada", "https://1.bp.blogspot.com/-wK1IAcNd4SE/VhmjE16xC2I/AAAAAAAAMkU/D7Rhr64Ta40/s1600/QUANDO%2B1.jpg"));

			stockMovementRepository.save(new StockMovement( item, 5, true));

			logsRepository.save(new Logs("save stock inicial", "Nome: Ninhada Qtde: 5"));
		};
	}
}
