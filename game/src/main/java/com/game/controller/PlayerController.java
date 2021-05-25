package com.game.controller;

import com.game.models.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

        @Autowired
        private PlayerService playerService;

        public PlayerController(PlayerService playerService) {
            this.playerService= playerService;
        }

        @GetMapping("")
        public @ResponseBody ResponseEntity<List<Player>> getPlayersList(@RequestParam Map<String, String> params){
            if (params.isEmpty()) {
                return new ResponseEntity<>(playerService.findAll(PageRequest.of(0, 3)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(playerService.findByParams(params), HttpStatus.OK);
            }
        }

    @GetMapping("/count")
    public @ResponseBody Integer getPlayerCount(@RequestParam Map<String, String> params){
        if (params.isEmpty()) {
            return playerService.count();
        } else {
            return playerService.countByParams(params);
        }
    }
        @PostMapping("")
        @ResponseStatus(HttpStatus.OK)
        public @ResponseBody ResponseEntity<Player> addPlayer(@RequestBody Map<String, String> params){
            if (!playerService.isAllParamsFound(params) || !playerService.isParamsValid(params)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            Player player = playerService.add(params);
            ResponseEntity<Player> result = player == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : new ResponseEntity<>(player, HttpStatus.OK);
            return result;
        }

        @GetMapping("{id}")
        public @ResponseBody ResponseEntity<Player> getPlayer(
                @PathVariable String id
        ){
            try {
                long longId = Long.parseLong(id);
                if (!playerService.isIdValid(longId)) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                if (!playerService.existsById(longId)) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity<>(playerService.findById(longId), HttpStatus.OK);
                }
            } catch (NullPointerException | IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        @PostMapping("{id}")
        public @ResponseBody
        ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Map<String, String> params){
            ResponseEntity<Player> badResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            ResponseEntity<Player> nfResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (params.isEmpty()) {
                Player player = playerService.findById(id);
                return new ResponseEntity<>(player, HttpStatus.OK);
            }
            if (!playerService.isIdValid(id) || !playerService.validParamsForUpdate(params)) {
                return badResponse;
            }
            Player result = playerService.updatePlayer(id, params);
            if (result == null) {
                return nfResponse;
            } else {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        }

        @DeleteMapping("{id}")
        public @ResponseBody
        ResponseEntity<Player> deletePlayer(@PathVariable String id){
            ResponseEntity<Player> okResponse = new ResponseEntity<>(HttpStatus.OK);
            ResponseEntity<Player> badResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            ResponseEntity<Player> nfResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);//404
            try {
                long longId = Long.parseLong(id);
                if (!playerService.isIdValid(longId)) return badResponse;
                boolean result = playerService.deleteById(longId);
                if (result) return okResponse;
                else return nfResponse;
            } catch (NullPointerException | IllegalArgumentException e) {
                return badResponse;
            }
        }
    }
