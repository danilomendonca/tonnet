package simulator;

import network.*;
import measurement.*;

public class Simulation {

  private double arrivedRate;
  private int numReply;
  private double holdRate;
  private int totalNumberOfRequests;
  private String wAAlgorithm;
  private int simulationType;
  private Mesh mesh;

  /**
   * Constroi um obj Simulation.
   *
   * @param holdRate double
   * @param arrivedRate double
   * @param numberReq int
   * @param simulationType int
   * @param wAssing String
   */
  public Simulation(double holdRate, double arrivedRate,
                    int numberReq,
                    int simulationType, String wAssing) {
    this.holdRate = holdRate;
    this.arrivedRate = arrivedRate;
    this.numReply = numReply;
    this.totalNumberOfRequests = numberReq;
    this.simulationType = simulationType;
    this.wAAlgorithm = wAssing;
  }

//------------------------------------------------------------------------------
  public void setMesh(Mesh mesh) {
    this.mesh = mesh;
    this.mesh.getMeasurements().setArrivedRate(this.arrivedRate);
    this.mesh.getMeasurements().setReplication(this.numReply);
  }

//------------------------------------------------------------------------------
  public int getSimulationType() {
    return this.simulationType;
  }
  //------------------------------------------------------------------------------
  public void setArrivedRate(double arrivedRate) {
    this.arrivedRate = arrivedRate;
  }
  //------------------------------------------------------------------------------
  public void setnumReply(int numReply) {
    this.numReply = numReply;
  }
  //------------------------------------------------------------------------------
  public double getArrivedRate() {
    return this.arrivedRate;
  }
//------------------------------------------------------------------------------
  public double getHoldRate() {
    return this.holdRate;
  }
//------------------------------------------------------------------------------
    public Mesh getMesh() {
      return this.mesh;
  }
//------------------------------------------------------------------------------
    public int getTotalNumberOfRequest() {
      return this.totalNumberOfRequests;
  }
//------------------------------------------------------------------------------
  public void setTotalNumberOfRequest(int totalNumberOfRequests) {
    this.totalNumberOfRequests = totalNumberOfRequests;
  }
//------------------------------------------------------------------------------
    public String getWAAlgorithm() {
      return this.wAAlgorithm;
  }

}

