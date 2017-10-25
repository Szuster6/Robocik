package robot;


    import javax.swing.*;
    import javax.vecmath.*;
    import com.sun.j3d.utils.behaviors.mouse.*;
    import com.sun.j3d.utils.geometry.*;
    import javax.media.j3d.*;
    import java.awt.*;
    import com.sun.j3d.utils.universe.SimpleUniverse;
    import javax.media.j3d.Transform3D;
    import javax.vecmath.Vector3f;
    import javax.vecmath.Point3d;
    import com.sun.j3d.utils.image.TextureLoader;
    import java.util.Enumeration;


public class Main extends JFrame{
    
    private BranchGroup scena;
    
    private zachowanie r_korpus;
    private zachowanie r_ramie;
    private zachowanie r_wysiegnik;
    private zachowanie ch1_rot;
    private zachowanie ch2_rot;
    private zachowanie r_obiekt;
    
    private TransformGroup transformacja_k;
    private TransformGroup transformacja_r;
    private TransformGroup transformacja_w;
    private TransformGroup transformacja_ch;
    private TransformGroup transformacja_ch2;
    private TransformGroup transformacja_ch3;
    private TransformGroup transformacja_ch4;
    private TransformGroup transformacja_podloga;
    private TransformGroup obrot_swiata;
    private TransformGroup transformacja_obiekt;
    
    
    private Transform3D p_korpus;
    private Transform3D p_ramie;
    private Transform3D p_wysiegnik;
    private Transform3D p_chwytak;
    private Transform3D p_chwytak2;
    private Transform3D p_chwytak3;
    private Transform3D p_chwytak4;
    private Transform3D p_podloga;
    private Transform3D p_obiekt;
    
    private float g=0;
    private float l=0;
    private float v=0;
    private float o=0;
    
  public class zachowanie extends Behavior {

        private TransformGroup transformGroup;
        private Transform3D trans = new Transform3D();
        private Transform3D trans1 = new Transform3D();
        private WakeupCriterion criterion;

   

        private final int ROTY = 1;
      

      zachowanie(TransformGroup tg) {
            transformGroup = tg;
            tg.getTransform(trans1);
        }

        
        public void initialize() {
            criterion = new WakeupOnBehaviorPost(this, ROTY);
            wakeupOn(criterion);
            

        }

        
        public void processStimulus(Enumeration criteria) {

            trans1.mul(trans);
            transformGroup.setTransform(trans1);
            wakeupOn(criterion);
            
        }

        
        void trans(Transform3D hlp_trans) {
            trans = hlp_trans;
            postId(ROTY);
               }
        
       }   
    
    
    public Main(){

        //okno programu
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        
        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setPreferredSize(new Dimension(600,600));

        add(canvas3D);
        pack();
        setVisible(true);
        setResizable(false);
        
        //tworzenie sceny
        BranchGroup scena = utworzScene();
	scena.compile();
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        Transform3D przesuniecie_obserwatora = new Transform3D();
        Transform3D rot_obs = new Transform3D();
        rot_obs.rotY((float)(-Math.PI/5));
        przesuniecie_obserwatora.set(new Vector3f(-3.0f,4.0f,3.5f));
        przesuniecie_obserwatora.mul(rot_obs);
        rot_obs.rotX((float)(-Math.PI/5));
        przesuniecie_obserwatora.mul(rot_obs);
       
        

        simpleU.getViewingPlatform().getViewPlatformTransform().setTransform(przesuniecie_obserwatora);
        simpleU.addBranchGraph(scena);
    }
    BranchGroup utworzScene(){
     scena = new BranchGroup();
        BoundingSphere bounds =  new BoundingSphere(new Point3d(0.0d,0.0d,0.0d), 10.0d);

        //obracanie przestrzeni
        obrot_swiata = new TransformGroup();
        obrot_swiata.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        obrot_swiata.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        MouseRotate mrotat = new MouseRotate();
        mrotat.setTransformGroup(obrot_swiata);
        mrotat.setSchedulingBounds(bounds);
       
        obrot_swiata.addChild(mrotat);
        scena.addChild(obrot_swiata);
        
        TransformGroup obrot_animacja = new TransformGroup();
        obrot_animacja.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        scena.addChild(obrot_animacja);

        //swiatło tła
        Color3f kolor_swiatla_tla = new Color3f(0.0f, 1.0f, 1.0f);
        AmbientLight swiatlo_tla = new AmbientLight(kolor_swiatla_tla);
        swiatlo_tla.setInfluencingBounds(bounds);
        scena.addChild(swiatlo_tla);
        
        //światła kierunkowe
   

      DirectionalLight lightD = new DirectionalLight();
      lightD.setInfluencingBounds(bounds);
      lightD.setDirection(new Vector3f(0.0f, -0.5f, -0.5f));
      lightD.setColor(new Color3f(0.0f, 1.0f, 1.0f));
      scena.addChild(lightD);
        
      DirectionalLight lightC = new DirectionalLight();
      lightC.setInfluencingBounds(bounds);
      lightC.setDirection(new Vector3f(0.0f, 0.0f, 0.5f));
      lightC.setColor(new Color3f(0.0f, 1.0f, 1.0f));
      scena.addChild(lightC);
          
      
      DirectionalLight lightB = new DirectionalLight();
      lightB.setInfluencingBounds(bounds);
      lightB.setDirection(new Vector3f(0.0f, 1.0f, 0.5f));
      lightB.setColor(new Color3f(0.0f, 1.0f, 1.0f));
      scena.addChild(lightB);  
        
      DirectionalLight lightA = new DirectionalLight();
      lightA.setInfluencingBounds(bounds);
      lightA.setDirection(new Vector3f(0.0f, -1.0f, 0.5f));
      lightA.setColor(new Color3f(0.0f, 1.0f, 1.0f));
      scena.addChild(lightA);  
        
        //tworzenie obiektu
        Appearance wyglad_obiekt = new Appearance();
        TextureLoader loader = new TextureLoader("tekstury/obiekt3.jpg",null);
        ImageComponent2D image = loader.getImage();
            
        Texture2D obiekt = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());
        obiekt.setImage(0, image);
        obiekt.setBoundaryModeS(Texture.WRAP);
        obiekt.setBoundaryModeT(Texture.WRAP);
        wyglad_obiekt.setTexture(obiekt);
        com.sun.j3d.utils.geometry.Box BoxObiekt = new com.sun.j3d.utils.geometry.Box(0.15f, 0.2f, 0.2f, com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS| com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wyglad_obiekt);
        BoxObiekt.setCollidable(false);
        
        p_obiekt = new Transform3D();
        p_obiekt.set(new Vector3f(-1.4f, -0.2f, -0.9f));
        transformacja_obiekt = new TransformGroup(p_obiekt);
        transformacja_obiekt.addChild(BoxObiekt);
        obrot_swiata.addChild(transformacja_obiekt);
       
        //podłoga
        Appearance wyglad_podlogi = new Appearance();
        loader = new TextureLoader("tekstury/gg.jpg",null);
        image = loader.getImage();

        Texture2D ziemia = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());

        ziemia.setImage(0, image);
        ziemia.setBoundaryModeS(Texture.WRAP);
        ziemia.setBoundaryModeT(Texture.WRAP);
        wyglad_podlogi.setTexture(ziemia);
        com.sun.j3d.utils.geometry.Box BoxPodloga = new com.sun.j3d.utils.geometry.Box(1.7f, 0.1f, 1.7f, com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS| com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wyglad_podlogi);
        BoxPodloga.setCollidable(false);


        p_podloga = new Transform3D();
        p_podloga.set(new Vector3f(0.0f, -0.50f, 0.0f));
        transformacja_podloga = new TransformGroup(p_podloga);
        transformacja_podloga.addChild(BoxPodloga);
        obrot_swiata.addChild(transformacja_podloga);
 
        
        
        //tworzenie korpusu
        Appearance  wygladKorpusu = new Appearance(); 
        
        loader = new TextureLoader("tekstury/stal3.jpg",null);
        image = loader.getImage();

        Texture2D korpus = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());

        korpus.setImage(0, image);
        korpus.setBoundaryModeS(Texture.CLAMP_TO_EDGE);
        korpus.setBoundaryModeT(Texture.CLAMP_TO_EDGE);
        wygladKorpusu.setTexture(korpus);
        com.sun.j3d.utils.geometry.Cylinder BoxKorpus = new com.sun.j3d.utils.geometry.Cylinder(0.15f, 2f,com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS| com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wygladKorpusu);
        BoxKorpus.setCollidable(true);
        

        //pozycjonowanie korpusu
      
        p_korpus = new Transform3D();
        p_korpus.set(new Vector3f(0.0f, 0.55f, 0.0f));
        transformacja_k = new TransformGroup(p_korpus);
        transformacja_k.addChild(BoxKorpus);
        transformacja_k.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformacja_k.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        obrot_swiata.addChild(transformacja_k);
  
        //tworzenie ramienia
        Appearance  wygladRamienia = new Appearance(); 
        
        loader = new TextureLoader("tekstury/stal.jpg",null);
        image = loader.getImage();

        Texture2D ramie = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());

        ramie.setImage(0, image);
        ramie.setBoundaryModeS(Texture.WRAP);
        ramie.setBoundaryModeT(Texture.WRAP);
        wygladRamienia.setTexture(ramie);
        com.sun.j3d.utils.geometry.Cylinder BoxRamienia = new com.sun.j3d.utils.geometry.Cylinder(0.14f, 2.0f,com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS| com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wygladRamienia);
        BoxRamienia.setCollidable(false);
       
        
//        Appearance  wygladRamienia = new Appearance();
//        wygladRamienia.setColoringAttributes(new ColoringAttributes(0.0f,0.0f,0.0f,ColoringAttributes.NICEST));
//        Cylinder ramie = new Cylinder(0.15f, 1.3f);

        //pozycjonowanie ramienia

        Transform3D  tmp_rot2      = new Transform3D();       //ustawienie ramienia w poziomie
        tmp_rot2.rotX(Math.PI/2);
        tmp_rot2.setTranslation(new Vector3f (0.0f,1.1f,-0.1f));
       
        p_ramie = new Transform3D();
        p_ramie.set(tmp_rot2);
        transformacja_r = new TransformGroup(p_ramie);
        transformacja_r.addChild(BoxRamienia);
        transformacja_r.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformacja_r.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformacja_k.addChild(transformacja_r);
        
        //tworzenie wysięgnika
        Appearance wygladwys = new Appearance(); 
        
        loader = new TextureLoader("tekstury/stal.jpg",null);
        image = loader.getImage();

        Texture2D wys = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                        image.getWidth(), image.getHeight());

        wys.setImage(0, image);
//        wys.setBoundaryModeS(Texture.CLAMP);
//        wys.setBoundaryModeT(Texture.CLAMP);
        wygladwys.setTexture(wys);
        com.sun.j3d.utils.geometry.Cylinder Boxwys = new com.sun.j3d.utils.geometry.Cylinder(0.12f, 1f,com.sun.j3d.utils.geometry.Box.GENERATE_NORMALS_INWARD   | com.sun.j3d.utils.geometry.Box.GENERATE_TEXTURE_COORDS, wygladwys);
        Boxwys.setCollidable(false);
        
//        Appearance  wygladwys = new Appearance();
//        
//        wygladwys.setColoringAttributes(new ColoringAttributes(1.0f,1.0f,0.0f,ColoringAttributes.NICEST));
//        Cylinder wys = new Cylinder(0.1f, 1.0f);

        
        //pozycjonowanie wysięgnika
        
         p_wysiegnik = new Transform3D();
         p_wysiegnik.set(new Vector3f(0.0f,1.0f,0.0f));
       
         transformacja_w = new TransformGroup(p_wysiegnik);
         transformacja_w.addChild(Boxwys);
         transformacja_w.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         transformacja_w.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
         //transformacja_w.addChild(wys);
         transformacja_r.addChild(transformacja_w);
        

         //tworzenie chwytaka1
         
         Cylinder chwyt1= new Cylinder (0.05f,0.3f);
         
         //pozycja chywtaka1
         Transform3D  tmp_rot3      = new Transform3D();       
         tmp_rot3.rotZ(Math.PI/-3);
         tmp_rot3.setTranslation(new Vector3f (0.15f,0.5f,0.0f));
         
         p_chwytak = new Transform3D();
         p_chwytak.set(tmp_rot3);
         transformacja_ch = new TransformGroup(p_chwytak);
         transformacja_ch.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         transformacja_ch.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
         transformacja_ch.addChild(chwyt1);
         transformacja_w.addChild(transformacja_ch);
         
         //tworzenie chwytaka2
         Cylinder chwyt2= new Cylinder (0.05f,0.3f);
         
         //pozycja chywtaka2
         Transform3D  tmp_rot4      = new Transform3D();       
         tmp_rot4.rotZ(Math.PI/3);
         tmp_rot4.setTranslation(new Vector3f (-0.15f,0.5f,0.0f));
        
         p_chwytak2 = new Transform3D();
         p_chwytak2.set(tmp_rot4);
         transformacja_ch2 = new TransformGroup(p_chwytak2);
         transformacja_ch2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         transformacja_ch2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
         transformacja_ch2.addChild(chwyt2);
         transformacja_w.addChild(transformacja_ch2);
         
        //  tworzenie chwytaka3
         
         Cylinder chwyt3= new Cylinder (0.05f,0.25f);
         
        // pozycja chywtaka3
         Transform3D  tmp_rot5      = new Transform3D();       
         tmp_rot5.rotZ(Math.PI/3);
         tmp_rot5.setTranslation(new Vector3f (-0.1f,0.15f,0.0f));
         
         p_chwytak3 = new Transform3D();
         p_chwytak3.set(tmp_rot5);
         transformacja_ch3 = new TransformGroup(p_chwytak3);
         transformacja_ch3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         transformacja_ch3.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
         transformacja_ch3.addChild(chwyt3);
         transformacja_ch.addChild(transformacja_ch3);
         
         //tworzenie chwytaka4
         
         Cylinder chwyt4= new Cylinder (0.05f,0.25f);
         
         //pozycja chywtaka4
         Transform3D  tmp_rot6      = new Transform3D();       
         tmp_rot6.rotZ(Math.PI/-3);
         tmp_rot6.setTranslation(new Vector3f (0.1f,0.15f,0.0f));
         
         p_chwytak4 = new Transform3D();
         p_chwytak4.set(tmp_rot6);
         transformacja_ch4 = new TransformGroup(p_chwytak4);
         transformacja_ch4.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         transformacja_ch4.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
         transformacja_ch4.addChild(chwyt4);
         transformacja_ch2.addChild(transformacja_ch4);
         
         //pozycja obiekt
        
         tmp_rot5 = new Transform3D();       
         tmp_rot5.rotZ(Math.PI/3);
         tmp_rot5.setTranslation(new Vector3f (-0.1f,0.15f,1.0f));
         
         p_obiekt = new Transform3D();
         p_obiekt.set(tmp_rot5);
         transformacja_obiekt = new TransformGroup(p_obiekt);
         transformacja_obiekt.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
         transformacja_obiekt.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
         //transformacja_obiekt.addChild(chwyt);
         transformacja_ch.addChild(ch1_rot);
         transformacja_ch2.addChild(ch2_rot);
     
         
         //obracanie korpusem
         r_korpus = new zachowanie(transformacja_k);
         r_korpus.setSchedulingBounds(bounds);
         transformacja_k.addChild(r_korpus);
        
        //ruch ramieniem
         r_ramie = new zachowanie(transformacja_r);
         r_ramie.setSchedulingBounds(bounds);
         transformacja_r.addChild(r_ramie);
        
         
        //wciąganie wysięgnika
         r_wysiegnik = new zachowanie(transformacja_w);
         r_wysiegnik.setSchedulingBounds(bounds);
         transformacja_w.addChild(r_wysiegnik);

         //ruszanie chwytakiem
          ch1_rot = new zachowanie(transformacja_ch);
          ch1_rot.setSchedulingBounds(bounds);
          transformacja_ch.addChild(ch1_rot);
          
          ch2_rot = new zachowanie(transformacja_ch2);
          ch2_rot.setSchedulingBounds(bounds);
          transformacja_ch2.addChild(ch2_rot);
         
         
         
         
        return scena;

    }
  
    public static void main(String[] args) {
        
        Menu menu= new Menu();
        menu.setVisible(true);
    }
   
    
    //obrót podstawy kąt względem osi
    public void r_korpusu(double angle){
           Transform3D trans3d = new Transform3D();
           trans3d.rotY(angle);
           r_korpus.trans(trans3d);
          
                  }
    public void r_ramienia(double angle){
           Transform3D trans3d = new Transform3D();
           trans3d.rotX(angle);
                      
           r_ramie.trans(trans3d);
           
          
                  }
    public void r_wysiegnika(double angle){
           Transform3D trans3d = new Transform3D();
           trans3d.rotX(angle);
           r_wysiegnik.trans(trans3d);
          
                  }
    
    //ruch ramienia góra-dół (przesuniecie go o Wektor)
//           public void r_ramienia(boolean kierunek){
//           Transform3D trans3d = new Transform3D();
//           if((g<1.0) && (kierunek == false)){
//                trans3d.set(new Vector3f(0.0f,0.0f,0.1f));
//                r_ramie.trans(trans3d);
//                g=g+0.1f;
//           }else{
//               if((g>-0.6) && (kierunek==true)){
//                trans3d.set(new Vector3f(0.0f,0.0f,-0.1f));
//                r_ramie.trans(trans3d);
//                g=g-0.1f;
//               }}}
    

   //wyciąganie wysięgnika   (przesuniecie o Wektor)
//     public void r_wysiegnika(boolean kierunek){
//           Transform3D trans3d = new Transform3D();
//           if((l<0.8) && (kierunek == false)){
//                trans3d.set(new Vector3f(0.0f,0.1f,0.0f));
//                r_wysiegnik.trans(trans3d);
//                l=l+0.1f;
//           }else{
//               if((l>0.1) && (kierunek==true)){
//                trans3d.set(new Vector3f(0.0f,-0.1f,0.0f));
//                r_wysiegnik.trans(trans3d);
//                l=l-0.1f;
//               }
//           }}
//ruszanie jedna czescia chwytaka (obrot wzglem osi)
          
           public void ch_rot(boolean kierunek){
           Transform3D tmp_t3d = new Transform3D();
           transformacja_ch.getTransform(tmp_t3d);
           Matrix3f tmp_mz3f_ch_rot =new Matrix3f();
           tmp_t3d.get(tmp_mz3f_ch_rot);
           
           v=tmp_mz3f_ch_rot.getM10();
           
           Transform3D trans3d = new Transform3D();
                 
           if((kierunek ==false) && (v<-0.5)){
            trans3d.rotZ(0.1);
              ch1_rot.trans(trans3d);
           }else {
               if((kierunek==true) && (v>-0.8)){
            trans3d.rotZ(-0.1);
            ch1_rot.trans(trans3d);
               }
           }
       }
  //ruszanie druga czescia chwytaka   (obrot wzgledem osi) 
                
            
           public void ch2_rot(boolean kierunek){
           Transform3D tmp_t3d = new Transform3D();
           transformacja_ch2.getTransform(tmp_t3d);
           Matrix3f tmp_mx3f_ch_rot =new Matrix3f();
           tmp_t3d.get(tmp_mx3f_ch_rot);
           
           o=tmp_mx3f_ch_rot.getM10();
           
           Transform3D trans3d = new Transform3D();
                 
           if((kierunek ==false) && (o<0.8)){
            trans3d.rotZ(0.1);
              ch2_rot.trans(trans3d);
           }else {
               if((kierunek==true) && (o>0.5)){
            trans3d.rotZ(-0.1);
            ch2_rot.trans(trans3d);
            
            
               }
           }
       }
      public void reset(){
          
          //powracanie wysiegnika
           Transform3D zero_w = new Transform3D();
           transformacja_w.getTransform(zero_w);
           zero_w.invert();
           zero_w.mul(p_wysiegnik);
           r_wysiegnik.trans(zero_w);
           l=0;
         //powracanie korpusu
           Transform3D zero_k = new Transform3D();
           transformacja_k.getTransform(zero_k);
           zero_k.invert();
           zero_k.mul(p_korpus);
           r_korpus.trans(zero_k);
        // powracanie ramienia
           Transform3D zero_r = new Transform3D();
           transformacja_r.getTransform(zero_r);
           zero_r.invert();
           zero_r.mul(p_ramie);
           r_ramie.trans(zero_r);
           g=0;
        // powracanie pierwszej czesci chwytaka   
          Transform3D zero_ch = new Transform3D();
          transformacja_ch.getTransform(zero_ch);
          zero_ch.invert();
          zero_ch.mul(p_chwytak);
          ch1_rot.trans(zero_ch);
        //powracanie drugiej czesci chwytaka
          Transform3D zero_ch2 = new Transform3D();
          transformacja_ch2.getTransform(zero_ch2);
          zero_ch2.invert();
          zero_ch2.mul(p_chwytak2);
          ch2_rot.trans(zero_ch2);
          
          
           
          
                        }
     
     
     

}
    
    
    
    
    
    


