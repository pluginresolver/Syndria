package minecade.dungeonrealms.HearthstoneMechanics;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.database.ConnectionPool;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Hearthstone {
    private Location tp_loc;
    private int timer_seconds;
    private String tp_name;
    private Player p;
    private String p_name;

	public Hearthstone(String p_name) {
        this.p_name = p_name;
        this.timer_seconds = 0;
        p = Bukkit.getPlayer(p_name);
        loadData(p_name);
        HearthstoneMechanics.hearthstone_map.put(p_name, this);
    }

    public void setPlayer(Player p) {
        this.p = p;
    }

    /**
     * This is called AsyncPlayerLoginEvent so no need to make it Async
     * 
     * @param p_name
     */
    public void loadData(String p_name) {
        try (PreparedStatement pst = ConnectionPool.getConnection().prepareStatement("SELECT * FROM hearthstone WHERE p_name = ?")) {
            pst.setString(1, p_name);
            ResultSet rst = pst.executeQuery();
            if (!rst.first()) {
                tp_loc = HearthstoneMechanics.spawn_map.get("Cyrennica");
                tp_name = "Cyrennica";
                sendInsertQuery();
                return;
            }
            tp_name = rst.getString("location_name");
            if (tp_name == null) {
                System.out.print("Location name was null for " + p_name);
            }
            tp_loc = HearthstoneMechanics.spawn_map.get(rst.getString("location_name"));
            setTimer(rst.getInt("timer"));
            // TODO: Download the data from tables and set their spawns
            System.out.print("[HearthstoneMechanics] Set " + p_name + " location to " + tp_name);
            pst.close();
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    public void setLocation(Location l) {
        this.tp_loc = l;
    }

    /**
     * This saves all their data in an Async task
     */
    public void sendInsertQuery() {
        try (PreparedStatement pst = ConnectionPool.getConnection().prepareStatement(
                "INSERT IGNORE INTO hearthstone (p_name, location_name, timer) VALUES (?, ?, 0) ON DUPLICATE KEY UPDATE location_name = ?")) {
            pst.setString(1, tp_name);
            pst.setString(2, "Cyrennica");
            pst.setString(3, tp_name);
            pst.executeUpdate();
            System.out.print("[HeartstoneMechanics] Saved " + p_name + "s Hearthstone data for the first time.");
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveData() {
        new BukkitRunnable() {
            public void run() {
                try (PreparedStatement pst = ConnectionPool.getConnection().prepareStatement(
                        "INSERT INTO hearthstone VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE location_name = ?, timer = ?;")) {
                    pst.setString(1, p_name);
                    pst.setString(2, tp_name);
                    pst.setInt(3, timer_seconds);
                    pst.setString(4, tp_name);
                    pst.setInt(5, timer_seconds);
                    pst.executeUpdate();
                    // System.out.print("[HeartstoneMechanics] Saved " + p.getName() + "s Hearthstone data.");
                    pst.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }

    public Location getLocation() {
        return tp_loc;
    }

    public Player getPlayer() {
        return p;
    }

    public void setTimer(int timer) {
        timer_seconds = timer;
    }

    public int getTimer() {
        return timer_seconds;
    }

    public void setLocationName(String name) {
        this.tp_name = name;
    }

    public String getName() {
        return tp_name;
    }
}