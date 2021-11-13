package club.eridani.cursa.gui.mainmenu.particle;

import net.minecraft.client.gui.ScaledResolution;
import org.apache.commons.lang3.RandomUtils;

public class Particle {
	
	public float x, y, radius, speed, ticks, opacity;
	
	public Particle(ScaledResolution sr, float radius, float speed){
		x = RandomUtils.nextFloat()*sr.getScaledWidth();
		y = RandomUtils.nextFloat()*sr.getScaledHeight();
		ticks = RandomUtils.nextFloat()*sr.getScaledHeight()/2;
		this.radius = radius;
		this.speed = speed;
	}

}
