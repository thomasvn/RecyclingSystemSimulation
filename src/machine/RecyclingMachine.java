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
    private double availableMoney;

    // Capacity Related Data Members
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

        this.availableMoney = 100; // TODO: Set these values to the constant file

        this.maxGlassLoad = Constants.MAX_GLASS_LOAD;
        this.maxMetalLoad = Constants.MAX_METAL_LOAD;
        this.maxPaperLoad = Constants.MAX_PAPER_LOAD;
        this.maxPlasticLoad = Constants.MAX_PLASTIC_LOAD;

        this.currentGlassLoad = this.currentMetalLoad = this.currentPaperLoad = this.currentPlasticLoad = 0;
    }

    public RecyclingMachine(){
        this.inTransaction = false;
        this.transactionTotal = 0;

    }

    // Start Transaction
    public void startTransaction() {
       t = new Transaction();
       setInTransaction(true);
    }

    // End Transaction
    public void endTransaction() {
        setTransactionTotal(t.getTransactionTotal());

        setNumGlassItems(t.getNumGlassItems());
        setNumMetalItems(t.getNumMetalItems());
        setNumPaperItems(t.getNumPaperItems());
        setNumPlasticItems(t.getNumPlasticItems());

        setCurrentGlassLoad(t.getGlassLoad());
        setCurrentMetalLoad(t.getMetalLoad());
        setCurrentPaperLoad(t.getPaperLoad());
        setCurrentPlasticLoad(t.getPlasticLoad());

        // TODO: Still need to fulfill the payout to the customer
        if (availableMoney < t.getTransactionTotal()) {
            t.setPayoutToCoupon();
        } else {
            t.isPayoutInCash();
        }

        setInTransaction(false);
    }

    // Cancel Transaction
    public void cancelTransaction() {
        // Removes reference to the current Transaction
        startTransaction();

        // We are not currently actually in transaction
        setInTransaction(false);
    }

    // Insert Recyclable Item
    public boolean addRecyclableItem(RecyclableItem r) throws Exception {
        if (inTransaction == false) {
            System.out.println("The machine is currently not in transaction mode.");
            return false;
        }

        // Check whether the item will fit inside the load
        if (r instanceof Glass) {
            if (r.getWeight() + getCurrentGlassLoad() > getMaxGlassLoad()) {
                return false;
            }

            int priceInCents = (int) (r.getWeight() * Constants.GLASS_PRICE);
            t.addGlassItem((Glass) r, priceInCents);

        } else if (r instanceof Metal) {
            if (r.getWeight() + getCurrentMetalLoad() > getMaxMetalLoad()) {
                return false;
            }

            int priceInCents = (int) (r.getWeight() * Constants.METAL_PRICE);
            t.addMetalItem((Metal) r, priceInCents);

        } else if (r instanceof Paper) {
            if (r.getWeight() + getCurrentPaperLoad() > getMaxPaperLoad()) {
                return false;
            }

            int priceInCents = (int) (r.getWeight() * Constants.PAPER_PRICE);
            t.addPaperItem((Paper) r, priceInCents);

        } else if (r instanceof Plastic) {
            if (r.getWeight() + getCurrentPlasticLoad() > getMaxPlasticLoad()) {
                return false;
            }

            int priceInCents = (int) (r.getWeight() * Constants.PLASTIC_PRICE);
            t.addPlasticItem((Plastic) r, priceInCents);

        } else {
            System.out.println("You didn't insert a recyclable item. You are a terrible person.");
            throw new Exception();
        }

        return true;
    }

    //todo CHECK THIS???
    public void empty(){
        currentGlassLoad = currentMetalLoad = currentPaperLoad = currentPlasticLoad = 0;
    }


    // Setters and Getters
    public double getMaxGlassLoad() {
        return maxGlassLoad;
    }

    public double getCurrentGlassLoad() {
        return currentGlassLoad;
    }

    public void setCurrentGlassLoad(double currentGlassLoad) {
        this.currentGlassLoad = currentGlassLoad;
    }

    public double getMaxMetalLoad() {
        return maxMetalLoad;
    }

    public double getCurrentMetalLoad() {
        return currentMetalLoad;
    }

    public void setCurrentMetalLoad(double currentMetalLoad) {
        this.currentMetalLoad = currentMetalLoad;
    }

    public double getMaxPaperLoad() {
        return maxPaperLoad;
    }

    public double getCurrentPaperLoad() {
        return currentPaperLoad;
    }

    public void setCurrentPaperLoad(double currentPaperLoad) {
        this.currentPaperLoad = currentPaperLoad;
    }

    public double getMaxPlasticLoad() {
        return maxPlasticLoad;
    }

    public double getCurrentPlasticLoad() {
        return currentPlasticLoad;
    }

    public void setCurrentPlasticLoad(double currentPlasticLoad) {
        this.currentPlasticLoad = currentPlasticLoad;
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

    public void setTransactionTotal(int transactionTotal) {
        this.transactionTotal = transactionTotal;
    }

    public int getNumPlasticItems() {
        return numPlasticItems;
    }

    public void setNumPlasticItems(int numPlasticItems) {
        this.numPlasticItems = numPlasticItems;
    }

    public int getNumPaperItems() {
        return numPaperItems;
    }

    public void setNumPaperItems(int numPaperItems) {
        this.numPaperItems = numPaperItems;
    }

    public int getNumGlassItems() {
        return numGlassItems;
    }

    public void setNumGlassItems(int numGlassItems) {
        this.numGlassItems = numGlassItems;
    }

    public int getNumMetalItems() {
        return numMetalItems;
    }

    public void setNumMetalItems(int numMetalItems) {
        this.numMetalItems = numMetalItems;
    }

    public double getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(double availableMoney) {
        this.availableMoney = availableMoney;
    }

    public MachineStatistics getMachineStatistics() {
        return machineStatistics;
    }

    public JSONObject toJSON(){
        JSONObject o = new JSONObject();
        try{
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
            o.put("maxPaperLoad",maxPaperLoad);
            o.put("currentPaperLoad", currentPaperLoad);
            o.put("maxPlasticLoad", maxPlasticLoad);
            o.put("currentPlasticLoad", currentPlasticLoad);
            o.put("machineStatistics", machineStatistics.tojSON());
        }catch (Exception e){
            e.printStackTrace();
        }

        return o;
    }

    public void fromJSON(JSONObject o){

        try{
            id = (int) o.get("id");
            xCoord = (int) o.get("xCoord");
            yCoord = (int) o.get("yCoord");
            numPlasticItems = (int) o.get("numPlasticItems");
            numPlasticItems = (int) o.get("numPlasticItems");
            numGlassItems = (int) o.get("numGlassItems");
            numMetalItems = (int) o.get("numMetalItems");
            availableMoney = (int) o.get("availableMoney");
            maxGlassLoad = (int) o.get("maxGlassLoad");
            currentGlassLoad = (int) o.get("currentGlassLoad");
            maxMetalLoad = (int) o.get("maxMetalLoad");
            currentMetalLoad = (int) o.get("currentMetalLoad");
            maxPaperLoad = (int) o.get("maxPaperLoad");
            currentPaperLoad = (int) o.get("currentPaperLoad");
            maxPlasticLoad = (int) o.get("maxPlasticLoad");
            currentPlasticLoad = (int) o.get("currentPlasticLoad");
            machineStatistics.fromJSON((JSONObject)o.get("machineStatistics"));


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
