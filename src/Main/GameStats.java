package Main;

import Utils.Stats;

public class GameStats {

    private static GameStats instance;

    private Stats stats;

    private GameStats() {
        instance = this;

        stats = Stats.find("gameStats");
    }

    public void save() { stats.save(); }

    public int getCoins() { return stats.readInt("coins", 0); }
    public void setCoins(int coins) { stats.write("coins", coins); }
    public void addCoins(int coins) { setCoins(coins + getCoins()); }

    static void createInstance() {
        if (instance == null)
            new GameStats();
    }

    public static GameStats instance() { return instance; }

}
