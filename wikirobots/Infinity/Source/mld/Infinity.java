package mld;
import robocode.*;
import java.awt.Color;

// --------------------------------------------------------------------------------------
// Infinity
//
// By Michael Dorgan
//
// Version 1.0 - A mixing pot of all my nanos into a melee killing machine
//
// Take Butterfly's gun, DustBunny's Radar, Eight's movement, Rat's conservatism, add
// some good old programmer inspiration, and here you are - Infinity.  It outlives the 
// other bots with intelligent target selection and a pattern that tends to keep it
// away from walls and enemy guns.  It even had room left for a color scheme.
// A really good Nano Melee bot!  
//
// Codesize 244
//
// Version 1.1
//
// Thanks to Dummy's comment, I found a stupid bug in my learning routine.  I also made my
// radar turn faster in this version (another oops).  Finally, he's slightly more aggresive
// with his shooting and leads slightly less.  A good melee bot.
//
// Codesize 246
//
// Version 1.2 (08/31/02)
//
// Updated gun to use UDTNB style shooting.  Changed power consumption to be linear instead
// of exponetial.  Range > 300 will always shoot 0.1.  Always shhot if we can now.  This
// leads to slightly more damage and removes the stupid fire if aimed boolean code.
// Overall, marginally better than before - but I freed up a ton of code space.  I may
// add special 1v1 movement into the next version to help in the final 1v1 battle.
//
// Version 1.3 (9/16/02)
//
// Changed from Infinity symbol movement to pure circle.   Fixed a small bug in my power 
// management routine which was causing less powerful shots than I wanted.  Basically, 
// I seen how effective circle movement is at this level and went that way.  Gem's
// idea of reversing when taking damage does well in keeping it out of larger furballs 
// while allowing it to stay close to bots not shooting at it.  I adopted this idea with
// a twist. I also am doing variable size circles depending on range.  This reduces wall
// damage and keeps me further from the center of the board in general.  The hitWall
// code is supposed to accelerate me for a tick so I don't hit the wall when I circle
// back, but it doesn't work quite right.  No big deal.  This guy can compete with Gem.
// I've also found that the more good 1v1 bots there are in the mix, the better Dustbunny
// tends to do.  I may go modify him next with a better power management routine :)
// 
// Codesize (with colors and some fluff code) 249 

//
// Version 1.4 (9/16/02)
//
// Ok, got off my butt and implemented a psuedo circular aim routine.  I'll post an 
// article explaining the math behind it with this bot post.  It's not perfect, but it
// does give me a slight edge on circle or arc moving bots and also allows better aim
// at plain linear bots.  Dropped the variable speed stuff and went back to constant
// circles.  I was able to keep my colors though (yeah!).  This bot does very well indeed
// in melee and may do well in 1v1 as well.   Basically the circle stuff, because of
// my linear interpolation of it, fall apart quickly at medium to long range - so we
// reduce power to make up for this.  This does no worse than normal linear lead or
// no lead at range > 400.  A good update with new technology to be tweaked.
//
// Codesize (with colors) 243
//
// Version 1.5 (9/17/02)
//
// I've played with the gun parameters quite a bit.  The result is a gun that is about 
// twice as accurate as last version!  Accurate enough that I increased the firepower
// a whole lot.  I also added code to make sure we don't overkill dead opponents - this
// cost us our neat paint job though.  I also fixed it so we don't continue to ram
// opponents half the time we hit them.  This really saves on the energy but costs about
// 10 bytes.  I may remove it in the future.  This bot is much improved over the previous
// version.  I real contendor for the melee title at week's end!
//
// Codesize (no colors) 249
//
// Version 1.6 (9/19/02)
//
// I added a few code saving techniques and added some power management such that my
// firepower is more based on remaining energy than opponents.  This results in higher
// power shots at the end when I can afford to shoot as such.  I added some code to lock
// the radar a few ticks before shooting.  This allows the deltaHeading code to work better.
// I also got color back in, but it cost me final blow code and slight inefficiencies in 
// radar turning and dodging.  I can always remove the color to get these back later - 
// but for now, I'll go for style over 100% efficiency.  This guy is about equal in
// ability to 1.5 - but has the paint job and style points :)
//
// Codesize (with Color) 249
//
// Version 1.7 (3/05/03)
// I looked around and found that Pinball's gun was my old linear/cicular aim - but optimized
// and better!  So I borrowed that back.  Next, after playing for a while, I found that a 
// rounded box pattern seems to be really hard for nano bots to hit, so I adopted that 
// for movement (ala gem).  Finally, I added code to not lock onto bots if they are way 
// away.  This helps with energy management.  An excellent upgrade - much better than the 
// previous version and now fairly competative in melee once again. Much thans to the 
// authors of Gem and Pinball for these ideas and improvements!
//
// Codesize (with Color) 249
//
// Version 2.0! (3/19/03)
// A new gun is in here.  It doesn linear aiming with decreased leading on increased target
// distance.  Works great.  I'm now doing infinite radar locking to save space and get me
// a better sacn arc.  I've added code to get me out of the center of the arena if I start
// there - works pretty well, but more space would make it even better.  Added much, much
// better target locking.  I now lock and fire really well on only the closest enemy.  This
// helps my survivability immensely!  A code saving trick I'm using is to call onRobotDeath()
// to reset my distance instead of assigning the global directly.  This saves a bunch of space.
// This guy is a really, really strong Nanobot Melee specialist.  A great bot that can be
// made smaller with only a little effort.  I'm very happy with it!
//
// Codesize (with colors) 246
//
// Version 2.1 (8-15-03)
// Same gun, slightly longer leg movement.  Better reach edge of arena code implemented.
// This code allows me usually to avoid being in the middle of a large group of enemies
// early and die.  Changed to not fire at foe unless less than 500 range.  lame but effective.
// Increased firepower value when I do fire.
//
// Codesize (with colors) 248

// Version 2.2 (4-28-09)
// Fixed a bug with first shot from out of range being horrible.  Nothing else.  Was already 
// really good, this just makes him a touch better - perhaps good enough for the crown.
//
// Codesize (with colors) 248
//
// --------------------------------------------------------------------------------------

public class Infinity extends AdvancedRobot
{
	// Constants - Beware, changing these may affect code size!
	static final double	BASE_MOVEMENT 		= 180;	// How far to move on each leg of our journey
	static final double	GUN_FACTOR  		= 500;	// How close someone needs to be before we lock radar - also used in leading.
	static final double	BASE_TURN	   		= Math.PI/2;	// Currently 90 degrees for a box pattern
	static final double	BASE_CANNON_POWER 	= 20;	// How hard to shoot in general 
		
	// Globals
	static double 	movement;
	static double 	lastHeading;
	static String	lastTarget;
	static double	lastDistance;
	
	public void run() 
	{
		// I had to make space for this...
		setColors(Color.white,Color.black,Color.green);
				
		// So we hit stationary targets.
		setAdjustGunForRobotTurn(true);

		// Set base movement huge so we get to edge of board.
		movement = Double.POSITIVE_INFINITY;
	
		// New target please
		onRobotDeath(null);

		// Use Infinite Radar lock technique to save space - no do while() loop.	
		turnRadarRight(Double.POSITIVE_INFINITY);
	}

	public void onHitWall(HitWallEvent e) 
	{
		// If we haven't reduced the distance, do so now.
		if(Math.abs(movement) > BASE_MOVEMENT)
		{
			movement = BASE_MOVEMENT;
		}
	}
		
	// When someone dies, reset our distance value so we don't get stuck aiming at a ghost
	public void onRobotDeath(RobotDeathEvent e) 
	{
		lastDistance = Double.POSITIVE_INFINITY;
	}
				
	// Do the majority of our bots code.
	public void onScannedRobot(ScannedRobotEvent e) 
	{
		double	absoluteBearing = e.getDistance();// - 1 / e.getEnergy();

		// If we've reached the end of a leg, turn and move.
		if(getDistanceRemaining() == 0)
		{
			setAhead(movement = -movement);
			setTurnRightRadians(BASE_TURN);
		}
		
		// Lock onto a new target if this one is closer than our current one.
		if(lastDistance > absoluteBearing)
		{
			lastDistance = absoluteBearing;
			lastTarget = e.getName();
		}
		
		// Lock/Fire if gun is somewhat cool and we're pointed at our selected target.
		if(lastTarget == e.getName())
		{
			if(getGunHeat() < 1 && absoluteBearing < GUN_FACTOR)
			{
				// Fire if gun is cool and we're pointed at target
				if(getGunHeat() == getGunTurnRemaining())
				{
					// No room for getOthers clause
//					setFireBullet(getOthers() * getEnergy() * BASE_CANNON_POWER / absoluteBearing);
					setFireBullet(getEnergy() * BASE_CANNON_POWER / absoluteBearing);
					// Reset distance
					onRobotDeath(null);
				}
													
			// Let this var be equal the the absolute bearing now...
			// and set the radar.
				setTurnRadarLeft(getRadarTurnRemaining());
			}
		
			absoluteBearing = e.getBearingRadians() + getHeadingRadians();

			// Ok, a new gun.  Linear aim with reduced target leading as distance from us increases.  Works really well
			// as we don't spray the walls as much at long distances.
			setTurnGunRightRadians(Math.asin(Math.sin(absoluteBearing - getGunHeadingRadians() + 
				(1 - e.getDistance() / 500) * 
				Math.asin(e.getVelocity() / 11) * Math.sin(e.getHeadingRadians() - absoluteBearing) )));					
		}
	}
}
