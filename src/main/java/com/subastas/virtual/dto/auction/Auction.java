package com.subastas.virtual.dto.auction;

import com.fasterxml.jackson.annotation.*;
import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.exception.custom.NotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Data
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auction {

    public static final Logger log = LoggerFactory.getLogger(Auction.class);

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_FINISHED = "FINISHED";

    private static final String CATEGORY_COMUN = "COMUN";
    private static final String CATEGORY_PLATA = "PLATA";
    private static final String CATEGORY_ORO = "ORO";
    private static final String CATEGORY_DIAMANTE = "DIAMANTE";

    private static final Long DURATION_IN_MILI = 10L*60*1000; // 10 minutes

    @Transient
    @JsonIgnore
    @ToString.Exclude
    private AuctionTask activeTask = new AuctionTask();


    @Transient
    @JsonIgnore
    @ToString.Exclude
    private Timer timer = new Timer();

    @Transient
    @JsonIgnore
    @ToString.Exclude
    private TimerTask startTask;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String category;

    @Column
    private String status;

    @Column
    private LocalDateTime activeUntil;

    @OneToMany(mappedBy = "auction")
    private List<Item> items = new ArrayList<>();

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(targetEntity = User.class, mappedBy = "auctions", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @Column()
    private Integer activeItem = -1;

    @Column
    private LocalDateTime startTime;

    public Auction(CreateAuctionRequest request) {
        this.title = request.getTitle();
        this.category = request.getCategory();
        this.status = STATUS_PENDING;
        this.category = CATEGORY_COMUN;
        this.startTime = request.getStartingTime();
    }

    public void startAuction() {
        log.info("startAuction - vamos a comenzar");
        // Esto activa el primer item
        activateNextItem();
        // Activamos la subasta e indicamos el momento del cierre
        this.status = STATUS_ACTIVE;
    }

    /**
     * Todas estas acciones modifica el auction y despues persistimos. Pero el item status
     * no podemos :( O SI????
     */
    public void activateNextItem() {

        // Finalizamos el item actual si existe
        if (activeItem != null && activeItem != -1) {
            log.info("Finalizando el item: {}", activeItem);
            Item item = this.getItems().stream()
                .filter(i -> i.getId() == activeItem).findFirst()
                .orElseThrow(() -> new NotFoundException("Problem, there should be an item here"));

            item.setStatus(STATUS_FINISHED); // TODO: Esto se puede sacar a un metodo del item para que el Auction no sepa de estados de Items.
        }

        // Buscamos el siguiente (o primer) item
        Optional<Item> nextItemOptional = this.getItems().stream()
            .filter(i -> i.getStatus().equals(Item.STATUS_PENDING) ||
                i.getStatus().equals(Item.STATUS_PROCESSING)).findFirst(); // Tenemos en cuenta Processing para tener items
                // TODO: Cuando tengamos algo que apruebe los items (o los carguemos como tal) sacar el processing

        // Si no tenemos más items, cerramos la subasta
        if (nextItemOptional.isEmpty()) {
            log.info("No encontramos más items para la subasta {}, cerramos.", id);
            this.closeAuction();
        } else { // Si tenemos otro item activamos la subasta para el mismo.
            this.activeItem = nextItemOptional.get().getId();
            log.info("Próximo item: {}. Ya se encuentra activo.", activeItem);

            nextItemOptional.get().setStatus(Item.STATUS_ACTIVE);
            startTimer();
        }

    }

    private void closeAuction() {
        log.info("Finishing auction: {}", id);
        activeItem = null;
        status = STATUS_FINISHED;
    }

    public void resetTimer() {
        log.info("Resetting timer for auction {} and item {}.", id, activeItem);
        activeTask.cancel();
        activeTask = new AuctionTask();
        timer.schedule(activeTask, DURATION_IN_MILI);
        this.activeUntil = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(5).minusHours(3);
    }

    public void startTimer() {
        log.info("Iniciamos el timer para la subasta: {}", id);
        activeTask = new AuctionTask();
        timer.schedule(activeTask, DURATION_IN_MILI);
        this.activeUntil = LocalDateTime.now().plusMinutes(5);
    }

    /**
     * When executed, it activates the next item for an auction,
     * or finishes said auction if no more items are available.
     */
    class AuctionTask extends TimerTask {
        @Override
        public void run() {
            log.info("AuctionTask triggered, moving forward with the auction.");
            activateNextItem();
        }
    };
}
