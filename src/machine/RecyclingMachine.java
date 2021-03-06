package machine;

import org.json.JSONObject;
import resources.Constants;
import recyclable.RecyclableItem;
import recyclable.Glass;
import recyclable.Metal;
import recyclable.Paper;
import recyclable.Plastic;
import statistics.Transaction;
import statistics.MachineStatistics;

/**
 * Contains RCM Functionality such ass starting transactions, to keeping track of all previous transactions and
 * the number of items inserted. It also contains information regarding money, and current weight load
 */
public class RecyclingMachine {
    // Containment of the MachineStatistics Class
    private MachineStatistics machineStatistics = new MachineStatistics();

    // Machine Information
    private int id;
    private int xCoord, yCoord;

    // Transaction Related Data Members
    private Transaction t;
    private boolean inTransaction;
    private int transactionTotal;
    private int numPlasticItems, numPaperItems, numGlassItems, numMetalItems;
    private int availableMoney; // In Cents

    // Capacity Related Data Members (Pounds)
    private double maxGlassLoad, currentGlassLoad;
    private double maxMetalLoad, currentMetalLoad;
    private double maxPaperLoad, currentPaperLoad;
    private double maxPlasticLoad, currentPlasticLoad;

    // Constructor
    public RecyclingMachine(int xCoord, int yCoord, int id) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.id = id;

        this.inTransaction = false;
        this.transactionTotal = 0;
        this.numGlassItems = this.numMetalItems = this.numPaperItems = this.numPlasticItems = 0;

        this.availableMoney = 50000;

        this.maxGlassLoad = Constants.MAX_GLASS_LOAD;
        this.maxMetalLoad = Constants.MAX_METAL_LOAD;
        this.maxPaperLoad = Constants.MAX_PAPER_LOAD;
        this.maxPlasticLoad = Constants.MAX_PLASTIC_LOAD;

        this.currentGlassLoad = this.currentMetalLoad = this.currentPaperLoad = this.currentPlasticLoad = 0.001;
    }

    public RecyclingMachine() {
        this.inTransaction = false;
        this.transactionTotal = 0;
    }

    // Start Transaction
    public void startTransaction() {
        t = new Transaction();
        setInTransaction(true);
    }

    // Cancel Transaction
    public void cancelTransaction() {
        // Removes reference to the current Transaction
        startTransaction();

        // We are not currently actually in transaction
        setInTransaction(false);
    }

    // End Transaction
    public void endTransaction() {
        this.transactionTotal = t.getTransactionTotal();

        this.numGlassItems += t.getNumGlassItems();
        this.numMetalItems += t.getNumMetalItems();
        this.numPaperItems += t.getNumPaperItems();
        this.numPlasticItems += t.getNumPlasticItems();

        this.currentGlassLoad += t.getGlassLoad();
        this.currentMetalLoad += t.getMetalLoad();
        this.currentPaperLoad += t.getPaperLoad();
        this.currentPlasticLoad += t.getPlasticLoad();

        if (this.availableMoney < this.transactionTotal) {
            t.setPayoutToCoupon();
            System.out.println("Available Money: " + Integer.toString(this.availableMoney));
        } else {
            this.availableMoney -= this.transactionTotal;
            System.out.println("Available Money: " + Integer.toString(this.availableMoney));
        }

        setInTransaction(false);
        machineStatistics.addTransaction(t);
    }

    // Insert Recyclable Item
    public boolean addRecyclableItem(RecyclableItem r) {
        if (inTransaction == false) {
            System.out.println("The machine is currently not in transaction mode.");
            return false;
        }

        // Check whether the item will fit inside the load
        if (r instanceof Glass) {
            if (r.getWeight() + this.currentGlassLoad + t.getGlassLoad() > this.maxGlassLoad) {
                return false;
            }

            int priceInCents = (int) (r.getWeight() * Constants.GLASS_PRICE);
            t.addGlassItem((Glass) r, priceInCents);

        } else if (r instanceof Metal) {
            if (r.getWeight() + this.currentMetalLoad + t.getMetalLoad() > this.maxMetalLoad) {
                return false;
            }

            int priceInCents = (int) (r.getWeight() * Constants.METAL_PRICE);
            t.addMetalItem((Metal) r, priceInCents);

        } else if (r instanceof Paper) {
            if (r.getWeight() + this.currentPaperLoad + t.getPaperLoad() > this.maxPaperLoad) {
                return false;
            }

            int priceInCents = (int) (r.getWeight() * Constants.PAPER_PRICE);
            t.addPaperItem((Paper) r, priceInCents);

        } else if (r instanceof Plastic) {
            if (r.getWeight() + this.currentPlasticLoad + t.getPlasticLoad() > this.maxPlasticLoad) {
                return false;
            }

            int priceInCents = (int) (r.getWeight() * Constants.PLASTIC_PRICE);
            t.addPlasticItem((Plastic) r, priceInCents);

        }

        return true;
    }

    // Empty out the recycling machine
    public void empty() {
        currentGlassLoad = currentMetalLoad = currentPaperLoad = currentPlasticLoad = 0;
        machineStatistics.justEmptied();
    }

    // Reload the money in the machine
    public void reload() {
        this.availableMoney = 50000;
    }

    // Setters and Getters
    public double getMaxGlassLoad() {
        return maxGlassLoad;
    }

    public double getCurrentGlassLoad() {
        return currentGlassLoad;
    }

    public double getCurrentTransactionGlassLoad() {
        return this.currentGlassLoad + t.getGlassLoad();
    }


    public double getMaxMetalLoad() {
        return maxMetalLoad;
    }

    public double getCurrentMetalLoad() {
        return currentMetalLoad;
    }

    public double getCurrentTransactionMetalLoad() {
        return this.currentMetalLoad + t.getMetalLoad();
    }


    public double getMaxPaperLoad() {
        return maxPaperLoad;
    }

    public double getCurrentPaperLoad() {
        return currentPaperLoad;
    }

    public double getCurrentTransactionPaperLoad() {
        return this.currentPaperLoad + t.getPaperLoad();
    }


    public double getMaxPlasticLoad() {
        return maxPlasticLoad;
    }

    public double getCurrentPlasticLoad() {
        return currentPlasticLoad;
    }

    public double getCurrentTransactionPlasticLoad() {
        return this.currentPlasticLoad + t.getPlasticLoad();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }


    public boolean isInTransaction() {
        return inTransaction;
    }

    public void setInTransaction(boolean inTransaction) {
        this.inTransaction = inTransaction;
    }

    public int getTransactionTotal() {
        return transactionTotal;
    }

    public int getCurrentTransactionTotal() {
        return t.getTransactionTotal();
    }


    public int getNumPlasticItems() {
        return numPlasticItems;
    }

    public int getNumPaperItems() {
        return numPaperItems;
    }

    public int getNumGlassItems() {
        return numGlassItems;
    }

    public int getNumMetalItems() {
        return numMetalItems;
    }


    public double getAvailableMoney() {
        return availableMoney;
    }

    public boolean getIsPayoutInCash() {
        return t.isPayoutInCash();
    }


    public MachineStatistics getMachineStatistics() {
        return machineStatistics;
    }

    public JSONObject toJSON() {
        JSONObject o = new JSONObject();
        try {
            o.put("id", id);
            o.put("xCoord", xCoord);
            o.put("yCoord", yCoord);
            o.put("numPlasticItems", numPlasticItems);
            o.put("numGlassItems", numGlassItems);
            o.put("numMetalItems", numMetalItems);
            o.put("availableMoney", availableMoney);
            o.put("maxGlassLoad", maxGlassLoad);
            o.put("currentGlassLoad", currentGlassLoad);
            o.put("maxMetalLoad", maxMetalLoad);
            o.put("currentMetalLoad", currentMetalLoad);
            o.put("maxPaperLoad", maxPaperLoad);
            o.put("currentPaperLoad", currentPaperLoad);
            o.put("maxPlasticLoad", maxPlasticLoad);
            o.put("currentPlasticLoad", currentPlasticLoad);
            o.put("machineStatistics", machineStatistics.tojSON());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return o;
    }

    public void fromJSON(JSONObject o) {

        try {
            id = (int) o.get("id");
            xCoord = (int) o.get("xCoord");
            yCoord = (int) o.get("yCoord");
            numPlasticItems = (int) o.get("numPlasticItems");
            numPlasticItems = (int) o.get("numPlasticItems");
            numGlassItems = (int) o.get("numGlassItems");
            numMetalItems = (int) o.get("numMetalItems");
            availableMoney = (int) o.get("availableMoney");
            maxGlassLoad = (int) o.get("maxGlassLoad");
            currentGlassLoad = (double) o.get("currentGlassLoad");
            maxMetalLoad = (int) o.get("maxMetalLoad");
            currentMetalLoad = (double) o.get("currentMetalLoad");
            maxPaperLoad = (int) o.get("maxPaperLoad");
            currentPaperLoad = (double) o.get("currentPaperLoad");
            maxPlasticLoad = (int) o.get("maxPlasticLoad");
            currentPlasticLoad = (double) o.get("currentPlasticLoad");
            machineStatistics.fromJSON((JSONObject) o.get("machineStatistics"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
