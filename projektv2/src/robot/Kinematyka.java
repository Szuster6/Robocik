package robot;

/**
 * @author Łukasz Bojarski && Cezary Hudzik
 */

public class Kinematyka extends java.lang.Object{
    
    private final double x, y, z;
    
    public static void main(String []args){
        
        Kinematyka res_xyz = kinematykaProsta(45,90,90);
        
        Kinematyka res_deg = kinematykaOdwrotna(7, 7, 20);
        
        System.out.println(res_xyz.getX() + " " + res_xyz.getY() + ' '+ res_xyz.getZ());
        
        System.out.println(res_deg.getX() + " " + res_deg.getY() + ' '+ res_deg.getZ());
        
        Kinematyka res_deg2 = kinematykaOdwrotna(Math.abs(res_xyz.getX()), Math.abs(res_xyz.getY()), res_xyz.getZ());
        
        System.out.println(res_deg2.getX() + " " + res_deg2.getY() + ' '+ res_deg2.getZ());
        
    }
    
    public Kinematyka(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
     
     
    public static Kinematyka kinematykaProsta(double a, double b, double c) {

//        System.out.println("Ustawione kąty: ("+ a + ", " + b + ", " + c + ")");

        double c2;
        double d1,d2,d3,d4,d5,d6;
        double L1,L2;
        double x,y,z;
        // KONWERSJA NA RADIANY
        double aa = Math.toRadians(a);
        double bb = Math.toRadians(b);
        double cc = Math.toRadians(c);

        // DANE ZNANE:
        L1 = 10; //DLUGOSC RAMIENIA PIERWSZEGO
        L2 = 10; // DLUGOSC RAMIENIA DRUGIEGO
        d2 = 10; // WYSOKOSC PODSTAWY

        // KONTY POMOCNICZE
        c2= c-90.0-b;
        double cc2 = Math.toRadians(c2); 

        // ROWNANIA KINEMATYKI PROSTEJ

            d3=L1*Math.sin(bb);
            d6=L2*Math.cos(cc2);
        z=d3+d2-d6;

            d4=L1*Math.cos(bb);
            d5=L2*Math.sin(cc2);
            d1=d4+d5;
        x=d1*Math.cos(aa);
        y=d1*Math.sin(aa);

        x = Math.round(x);
        y = Math.round(y);
        z = Math.round(z);
//        System.out.println("Wsp.:  "+x+"       "+y+"        "+z);


        return new Kinematyka(x, y, z); 
    }
     

    public static Kinematyka kinematykaOdwrotna(double x, double y, double z) {
    
   
    double o1, o2, o3, oo3_o2;
    double d1, a2, a3, xx;
    
    double ooo2, oo3, oo2;
    
    // DANE ZNANE:
    a2 = 10; //DLUGOSC RAMIENIA PIERWSZEGO
    a3 = 10; // DLUGOSC RAMIENIA DRUGIEGO
    d1 = 10; // WYSOKOSC PODSTAWY
    
    xx = Math.round(Math.sqrt( Math.pow(x, 2) + Math.pow(y, 2) ));
    z = z - d1;
   
    // ROWNANIA KINEMATYKI ODWROTNEJ
    o1 = 90.0 - Math.toDegrees(Math.atan2(x, y));
    o1 = Math.round(o1);
   
    //oo3 = ( Math.pow(a2, 2) + Math.pow(a3, 2) - Math.pow(xx, 2) - Math.pow(z, 2) ) / (2*a2*a3) ;
    //o3 = Math.toDegrees( Math.atan2(oo3, Math.sqrt(1 - Math.pow(oo3, 2))) );
    //o3 = Math.round(o3);
    oo3 = ( Math.pow(xx, 2) + Math.pow(z, 2) - Math.pow(a2, 2) - Math.pow(a3, 2) ) / (2*a2*a3) ;
    o3 =  Math.toDegrees( Math.acos(oo3) );
    o3 = Math.round(o3);
    o3 = 180.0 - o3;
    
    
//    ooo2 = a2 + ( a3*Math.cos(oo3_o2) );
//    oo2 = a3 * Math.sin(oo3_o2);
//    o2 = Math.toDegrees(Math.atan2(xx,z) - Math.atan2(ooo2,oo2));
//    o2 = Math.round(o2);
    ooo2 = (a3 * Math.sin( Math.toRadians(o3) )) / ( Math.sqrt(Math.pow(xx, 2) + Math.pow(z, 2)) );
    oo2 = z / xx;
    o2 = Math.toDegrees( Math.asin(ooo2) + Math.atan(oo2));
    o2 = Math.round(o2);
    
    System.out.println("_________________________");
//    System.out.println("Kąty: (" + o1 + ", "+o2+", "+ o3 +")");


    return new Kinematyka(o1, o2, o3);
}
   

     
}