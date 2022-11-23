   package jk;
   import robocode.*;
   import robocode.util.*;
   import java.awt.geom.*;
   import java.util.*;
   import java.awt.*;


    public class Waylander extends AdvancedRobot
   {
   
      static double direction = 1;
      final static double angleScale = 24;
      final static double velocityScale = 1;
      static double lastEnemyHeading;
      static double lastEnemyEnergy;
      static boolean flat;
      static boolean firstScan;
      static StringBuilder data = new StringBuilder();
      static double bulletVelocity;
     
     //DEBUG 
      // Vector points = new Vector();
   	
       public void run() {
         firstScan = true;
         
      	
        //  try{
      //       data.delete(60000, 80000);
      //    }
      //        catch(Exception e){}
             
             
         setAdjustRadarForGunTurn(true);
         //setAdjustRadarForRobotTurn(true);
         setAdjustGunForRobotTurn(true);
         
         while(true){
            turnRadarRight(Double.POSITIVE_INFINITY);
         }
      	
      }
   
   /**
    * onScannedRobot: What to do when you see another robot
    */
       public void onScannedRobot(ScannedRobotEvent e) {
         double headingRadians;
         double eDistance ;
         double eHeadingRadians;
         double absbearing=e.getBearingRadians()+ (headingRadians = getHeadingRadians());
         Point2D.Double myLocation = new Point2D.Double(getX(), getY());
         boolean rammer = (eDistance = e.getDistance()) < 100 || getTime() < 20; 
         
         Rectangle2D.Double field = new Rectangle2D.Double(17.9,17.9,764.1,564.1);
            
      		
      //movement \/  \/
         double v1, v2, offset = Math.PI/2 + 1 - eDistance/600;
         
         while(!field.
         contains(project(myLocation, v2 = absbearing + direction*(offset -= 0.02), 160))
         // contains(getX() + 160 * Math.sin(v2 = absbearing + direction * (offset -= .02)), getY() + 160 * Math.cos(v2))
         );
      
         
         if((flat && !rammer && 
         //Raiko's flattener
         // (Math.random() > Math.pow(v1 = 0.5952 * bulletVelocity /eDistance, v1))
         
         //a sbf style flattener - seems like it's easy to hit for FloodMini
         // getTime() >= lastTurnTime + eDistance/bulletVelocity*random
         
         //My flattener 
         Math.random() <  0.6*Math.sqrt(bulletVelocity/eDistance) - 0.04
         
         //Thorn's flattener - FloodGrapher shows it's flatter but it gives worse results!??!?
         // Math.random() < 0.65*Math.pow(eDistance/bulletVelocity, -0.65)
         
         // a fairly good linear approximation of Raiko and Thorn
         //  Math.random() < 1.1*bulletVelocity/eDistance + 0.03
         
         //Aristocles' flattener - light on codesize but easy to hit when the distance isn't 300-400
         // Math.random() <  2.374873835*bulletVelocity/eDistance
         
         ) || 
          offset < Math.PI/4 ) {
            direction = -direction;
            // lastTurnTime = getTime();
            // random = Math.random();
         }
         setTurnRightRadians(Math.tan(v2 -= headingRadians));
         
         double deltaE = (lastEnemyEnergy - (lastEnemyEnergy = e.getEnergy()));
         
         if((0 < deltaE && deltaE < 3.001) || flat || rammer){
            setAhead((37 + ((int)(deltaE - 0.50001))*11) *Math.signum(Math.cos(v2)));
         }
      	
         
         
      	// movement /\  /\
         
      	//gun \/   \/
         double w = lastEnemyHeading - (lastEnemyHeading = eHeadingRadians = e.getHeadingRadians());
         double speed = e.getVelocity();
         if(!firstScan)
            data.insert(0,(char)(w*angleScale))
               .insert(0,(char)(Math.round(speed*velocityScale)));
         
           
         int keyLength = Math.min(data.length(), Math.min((int)getTime(), 256));
         
         int index = -1;
         do{
            keyLength/=2;
            index = data.indexOf(data.substring(0, keyLength),((int)eDistance)/11)
               /2;//sorts out even/odd numbers
            
         }while(index == 0 && keyLength >  1);
         
         double bulletPower = rammer?3:Math.min(2,Math.min(getEnergy()/16, lastEnemyEnergy/2));
           
         Point2D.Double predictedPosition = project(myLocation, absbearing, eDistance);
         
         myLocation = project(myLocation, headingRadians , getVelocity());
         
         double db=0;
         double ww=eHeadingRadians; 
         do
         {
         // DEBUG!!
         // points.add(predictedLocation);
         
            if( index*(getRoundNum() + getTime()%2) > 0 ){
               speed = (((short)data.charAt(index*2))/velocityScale);
               w = (((short)data.charAt(index--*2 - 1)))/(angleScale) ;    
            }
         }while ((db+=(20-3*bulletPower))< myLocation.distance(predictedPosition = project(predictedPosition, ww-=w , speed)) 
         && field.contains(predictedPosition));         
         
         
         setTurnGunRightRadians(Utils.normalRelativeAngle(Math.atan2(predictedPosition.x - myLocation.x, predictedPosition.y - myLocation.y) - getGunHeadingRadians()));
         setFire(bulletPower);
      	
         setTurnRadarRightRadians(Math.sin(absbearing - getRadarHeadingRadians())*2);
               
         firstScan = false;
         
      	// gun /\  /\
      
      }
       public void onHitByBullet(HitByBulletEvent e){
         
         lastEnemyEnergy += 20 - (bulletVelocity = e.getVelocity());	
        
         // if(hits++ > 6 && getRoundNum() < 5 )
            // flat = true;
      }
   
       public void onDeath(DeathEvent e){
       
         if(getRoundNum() < 3)
            flat = true;
      
      }
       public void onBulletHit(BulletHitEvent e){
         // double bp = e.getBullet().getPower();
         // lastEnemyEnergy -= Math.max( bp*4 +,bp*6 - 2);
         lastEnemyEnergy -= 10;
         
      }
       Point2D.Double project(Point2D.Double location, double angle, double distance){
         return new Point2D.Double(location.x + distance*Math.sin(angle), location.y + distance*Math.cos(angle));
      }
   
       //DEBUG ONLY
   	/*
       public void onPaint(java.awt.Graphics2D g) {
         g.setColor(Color.green);
   
         
         
         for(int i = 0; i < points.size(); i++)
            g.drawOval((int)(((Point2D.Double)(points.get(i))).x),(int)(((Point2D.Double)(points.get(i))).y),
               2,2);
   				
   				         
         while(point.size() > 0)
   		   points.remove(0);
      }
   */
   
   }
