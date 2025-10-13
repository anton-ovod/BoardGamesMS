package org.example;

import org.example.enums.Category;
import org.example.enums.GameStatus;
import org.example.models.BoardGame;
import org.example.modules.boardgames.BoardGameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);

        BoardGameService service = context.getBean(BoardGameService.class);

        // --- CREATE ---
        BoardGame game = new BoardGame();
        game.setId(ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE));
        game.setTitle("Catan");
        game.setDescription("Trade and build");
        game.setMinPlayers(3);
        game.setMaxPlayers(4);
        game.setRecommendedAge(10);
        game.setPlayingTimeMinutes(90);
        game.setPublisher("Kosmos");
        game.setCategory(Category.CARD_GAME);
        game.setStatus(GameStatus.AVAILABLE);
        game.setRating(4.5);

        service.create(game);
        System.out.println("Created game: " + game.getTitle());

        // --- READ ---
        service.getAll().forEach(g -> System.out.println("Found game: " + g.getTitle()));

        // --- UPDATE ---
        game.setRating(4.7);
        service.update(game);
        System.out.println("Updated game rating to: " + game.getRating());

        // --- READ SINGLE ---
        BoardGame readGame = service.get(game.getId());
        System.out.println("Read single game: " + readGame.getTitle() + ", rating: " + readGame.getRating());

        // --- DELETE ---
        service.delete(game.getId());
        System.out.println("Deleted game: " + game.getTitle());

        // --- VERIFY DELETE ---
        service.getAll().forEach(g -> System.out.println("Remaining game: " + g.getTitle()));
    }
}
