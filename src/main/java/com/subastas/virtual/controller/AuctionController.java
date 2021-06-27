package com.subastas.virtual.controller;

import com.subastas.virtual.dto.auction.Auction;
import com.subastas.virtual.dto.auction.http.request.CreateAuctionRequest;
import com.subastas.virtual.dto.item.Item;
import com.subastas.virtual.dto.user.User;
import com.subastas.virtual.service.AuctionService;
import com.subastas.virtual.service.SessionService;
import java.net.URI;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/auctions")
public class AuctionController {

    Timer timer = new Timer();
    AuctionService auctionService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    /**
     * En caso de no tener un auto activate, podemos activar una subasta por este endpoint
     * @param auctionId
     * @return
     */
    @GetMapping("/{id}/activate")
    public ResponseEntity<Auction> activateAuction(@PathVariable("id") int auctionId) {
        return ResponseEntity.ok(auctionService.startAuction(auctionId));
    }

    /**
     * Creamos una nueva subaste. Si se indica una fecha de inicio, la subasta comienza automáticamente
     * @param request
     * @param session
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> createNewAuction(@RequestBody CreateAuctionRequest request, HttpSession session) {
        User user = SessionService.getUser(session);

        Auction auction = auctionService.createAuction(request);

        return ResponseEntity
                .created(URI.create(
                        String.format("/auctions/%d", auction.getId())))
                .body(auction);
    }

    @GetMapping("")
    public ResponseEntity<List<Auction>> getAuction(@RequestParam(name = "status", defaultValue = "") String status) {
        if ("".equalsIgnoreCase(status)) {
            return ResponseEntity.ok(auctionService.getAuctions());
        }

        auctionService.getAuctionByStatus(status);

        return ResponseEntity.ok(auctionService.getAuctionByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auction> getAuction(@PathVariable("id") int auctionId) {
        Auction auction = auctionService.getAuctionById(auctionId);

        return ResponseEntity.ok(auction);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<Item>> getAuctionItems(@PathVariable("id") int auctionId) {
        List<Item> auction = auctionService.getAuctionItemsById(auctionId);

        return ResponseEntity.ok(auction);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getUsersSubscribed(@PathVariable("id") int auctionId) {
        List<User> users = auctionService.getUsers(auctionId);
        return ResponseEntity.ok(users);
    }

    /**
     * Vincula la subasta con el usuario al que corresponde la sesión
     * @param auctionId
     * @param session
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> addAuctionToFavorite(@PathVariable("id") int auctionId, HttpSession session) {
        User user = SessionService.getUser(session);

        log.info("Attaching info for user {} and auction {}", user.getUsername(), auctionId);
        auctionService.addParticipant(auctionId, user);
        return ResponseEntity.ok(user.getId());
    }
}
