package models;

import com.google.gson.*;
import models.cards.Card;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class Player implements Comparable<Player> {
    public static PlayerSerializerForDeckDatabase playerSerializerForDeckDatabase = new PlayerSerializerForDeckDatabase();
    public static PlayerDeserializerForDeckDatabase playerDeserializerForDeckDatabase = null;
    private String username;
    private String password;
    private String nickname;
    private int picture = 1;
    private int score;
    private int rank;
    private int health;
    private transient Deck allPlayerCard;
    private transient ArrayList<Deck> allDeck = new ArrayList<>();
    private transient ArrayList<Deck> gameDecks = new ArrayList<>();
    private transient Deck activeDeck;
    private transient ArrayList<Card> inactiveCards = new ArrayList<>();
    private int money;
    private transient Board board = null;
    private transient String token;
    private transient int duelID;

    public Player(String username, String nickname, String password) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.picture = new Random().nextInt(10) + 1;
        this.score = 0;
        this.health = 8000;
        this.money = 32000000;
        setInactiveCards();
        allPlayerCard = new Deck(username + ".purchased-cards", this, false, true);
        Database.allPlayers.add(this);
    }

    public static PlayerSerializerForDeckDatabase getPlayerSerializerForDeck() {
        if (playerSerializerForDeckDatabase == null)
            playerSerializerForDeckDatabase = new PlayerSerializerForDeckDatabase();
        return playerSerializerForDeckDatabase;
    }

    public static PlayerDeserializerForDeckDatabase getPlayerDeserializerForDeck() {
        if (playerDeserializerForDeckDatabase == null)
            playerDeserializerForDeckDatabase = new PlayerDeserializerForDeckDatabase();
        return playerDeserializerForDeckDatabase;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public ArrayList<Deck> getGameDecks() {
        return gameDecks;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPicture() {
        return this.picture;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Deck getActiveDeck() {
        return activeDeck;
    }

    public void setActiveDeck(Deck activeDeck) {
        if (this.activeDeck != null)
            this.activeDeck.setActive(false);
        this.activeDeck = activeDeck;
        activeDeck.setActive(true);
    }

    public void increaseScore(int score) {
        setScore(this.score + score);
    }

    public void decreaseScore(int score) {
        setScore(this.score - score);
    }

    public ArrayList<Deck> getAllDeck() {
        if (allDeck == null)
            return (allDeck = new ArrayList<>());
        return allDeck;
    }

    public void setAllDeck(ArrayList<Deck> allDeck) {
        this.allDeck = allDeck;
    }

    public Deck getAllPlayerCard() {
        return allPlayerCard;
    }

    public void setAllPlayerCard(Deck allPlayerCard) {
        this.allPlayerCard = allPlayerCard;
    }

    public void addCardToAllPlayerCard(Card card) {
        this.allPlayerCard.getMainCards().add(card);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void deleteDeck(Deck deck) {
        allDeck.remove(deck);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public int compareTo(Player player) {
        if (this.score > player.score)
            return -1;
        if (this.score < player.score)
            return 1;
        if (this.nickname.compareTo(player.getNickname()) > 0)
            return -1;
        if (this.nickname.compareTo(player.getNickname()) < 0)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", score=" + score +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDuelID() {
        return duelID;
    }

    public void setDuelID(int duelId) {
        this.duelID = duelId;
    }

    public ArrayList<Card> getInactiveCards() {
        return inactiveCards;
    }

    public void setInactiveCards() {
        int sign = 0;
        for(Card card : allPlayerCard.mainCards){
            sign = 0;
            for(Deck deck : allDeck){
                if(deck.mainCards.contains(card) || deck.sideCards.contains(card)) sign = 1;
            }
            if(sign == 0) inactiveCards.add(card);
        }
    }
}

class PlayerSerializerForDeckDatabase implements JsonSerializer<Player> {
    @Override
    public JsonElement serialize(Player player, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(player.getUsername());
    }
}

class PlayerDeserializerForDeckDatabase implements JsonDeserializer<Player> {
    @Override
    public Player deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Database.getInstance().getPlayerByUsername(jsonElement.getAsString());
    }
}

