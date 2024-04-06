package RazorpayMachineRoundCode;

//question: Machine round:
//Design a car parking application
//  1.There are two categories of cars: SUV and hatchback.
//  2.Maintain a count of how many SUV and hatchback cars enter the premise.
//  3.Calculate the payment each car has to make based upon the rates as hatchback parking as 10 rupees per hour and SUV being 20 rupees an hour.
//  4.In case if hatchback occupancy is full then hatchback cars can occupy SUV spaces but with hatchback rates.
//  5.During exit there needs to be the system to inform the user how much they have to pay.
//  6.Admin can see all the cars which are parked in the system.

enum CarModelType {
    SUV, HATCH;
}

abstract class MyCar{
    CarModelType carModelType;
    int startTime;
    int rate;
    boolean isUsingSuvParking;
    public MyCar(CarModelType carModelType, int startTime){
        this.carModelType=carModelType;
        this.startTime=startTime;
        switch (carModelType){
            case SUV -> {this.rate=20;}
            case HATCH -> {this.rate=10;}
            default -> System.out.println("In valid car type as per now.");
        }
    }
}

class SuvCar extends MyCar{
    public SuvCar(CarModelType carModelType, int startTime) {
        super(carModelType, startTime);
    }
}
class HatchBackCar extends MyCar{
    public HatchBackCar(CarModelType carModelType, int startTime) {
        super(carModelType, startTime);
    }
}

//singleton design pattern.
class ParkingSystem{
  int totalCapacity;
  int suvCapacity;
  int hatchBackCapacity;
  private static ParkingSystem parkingSystem=null;

  public ParkingSystem(int suvCapacity, int hatchBackCapacity){
      this.suvCapacity=suvCapacity;
      this.hatchBackCapacity=hatchBackCapacity;
      this.totalCapacity=this.suvCapacity+this.hatchBackCapacity;
      System.out.println("Parking system created! with total capacity: "+ this.totalCapacity +
      " with initial suv capacity: "+ this.suvCapacity +" and hatchBackCapacity: "+ this.hatchBackCapacity);
  }

  //getting single instance of parking system.
  public static ParkingSystem getInstance(int suvCapacity, int hatchBackCapacity){
      if(parkingSystem==null){
          return new ParkingSystem(suvCapacity, hatchBackCapacity);
      }
      return parkingSystem;
  }
  public int getTotalCapacityLeft(){
      return this.suvCapacity+this.hatchBackCapacity;
  }
}

//factory design pattern
class ParkingFactory{

    //when the car reached to parking place will create the car instance with entry time.
    public static MyCar getCarInstance(CarModelType carModelType, int startTime){
        MyCar myCar=null;
        switch (carModelType){
            case SUV -> myCar = new SuvCar(carModelType, startTime);
            case HATCH -> myCar = new HatchBackCar(carModelType, startTime);
        }
        return myCar;
    }


    //After creating the car instance will park the car.
    public static void ParkTheCar(MyCar car, ParkingSystem parkingSystem){
        switch (car.carModelType){
            case SUV -> {
                if(parkingSystem.suvCapacity>0){
                    parkingSystem.suvCapacity-=1;
                }
                else {
                    System.out.println("No parking slot left for SUV.");
                }
            }
            case HATCH -> {
                if(parkingSystem.hatchBackCapacity>0){
                    parkingSystem.hatchBackCapacity-=1;
                }
                else if(parkingSystem.suvCapacity>0){
                    parkingSystem.suvCapacity-=1;
                    car.isUsingSuvParking=true;
                }
                else {
                    System.out.println("Parking is full.");
                }
            }
        }
    }

    //Check out the car by providing the car.
    public static int checkOutTheCar(MyCar car, ParkingSystem parkingSystem, int endTime){
        switch (car.carModelType){
            case SUV ->parkingSystem.suvCapacity+=1;
            case HATCH -> {
                if(car.isUsingSuvParking){
                    parkingSystem.suvCapacity+=1;
                }
                else{
                    parkingSystem.hatchBackCapacity+=1;
                }
            }
        }
        return (endTime-car.startTime)*car.rate;
    }
}

//main class to run the calls.
public class RazorpayMachine {
    public static void main(String[] args) {

        ParkingSystem parkingSystem = ParkingSystem.getInstance(5,1);
        MyCar suvCar1 = ParkingFactory.getCarInstance(CarModelType.SUV, 1);
        MyCar hatchback1 = ParkingFactory.getCarInstance(CarModelType.HATCH, 2);
        ParkingFactory.ParkTheCar(suvCar1, parkingSystem);
        System.out.println("Suv1 Parked at time 1");
        ParkingFactory.ParkTheCar(hatchback1, parkingSystem);
        System.out.println("Suv1 Parked at time 2");
        System.out.println("Parking capacity: "+ parkingSystem.getTotalCapacityLeft()+ "" +
                " Suv capacity: "+ parkingSystem.suvCapacity + " Hatchback Capacity: " +parkingSystem.hatchBackCapacity);
        MyCar hatchBack2 = ParkingFactory.getCarInstance(CarModelType.HATCH, 3);
        ParkingFactory.ParkTheCar(hatchBack2, parkingSystem);
        System.out.println("Suv1 Parked at time 3");
        System.out.println("Parking capacity: "+ parkingSystem.getTotalCapacityLeft()+ "" +
                " Suv capacity: "+ parkingSystem.suvCapacity + " Hatchback Capacity: " +parkingSystem.hatchBackCapacity);
        System.out.println("Checking out the 1st suv with total price of: "+ ParkingFactory.checkOutTheCar(suvCar1, parkingSystem,4));
        System.out.println("Checking out the 1st Hatchback with total price of: "+ ParkingFactory.checkOutTheCar(hatchback1, parkingSystem,4));
    }

}
