package club.eridani.cursa.gui.mainmenu.particle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import club.eridani.cursa.utils.RenderUtil;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ParticleManager{
	
	private float lastMouseX, lastMouseY;
	private List<Particle> array;

	public ParticleManager(){ array = new CopyOnWriteArrayList<>();}


	public void render(float mouseX, float mouseY, ScaledResolution sr) {
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.color(1, 1, 1, 1);
        float xOffset = sr.getScaledWidth()/2-mouseX;
        float yOffset = sr.getScaledHeight()/2-mouseY;
		for(array.size(); array.size() < (int)(sr.getScaledWidth()/19.2f); array.add(new Particle(sr, RandomUtils.nextFloat()*3 + 2, RandomUtils.nextFloat()*5 + 5)));
		List<Particle> toremove = new ArrayList<>();
		for(Particle p : array){
			if(p.opacity < 32){
				p.opacity += 2;
			}
			if(p.opacity > 32){
				p.opacity = 32;
			}
			Color c = new Color((int)255, (int)255, (int)255, (int)p.opacity);
			RenderUtil.drawCircle(p.x + Math.sin(p.ticks/2)*50 + -xOffset/5, (p.ticks*p.speed)*p.ticks/10 + -yOffset/5, p.radius*(p.opacity/32), -1);
			p.ticks += 0.05;// +(0.005*1.777*(GLUtils.getMouseX()-lastMouseX) + 0.005*(GLUtils.getMouseY()-lastMouseY));
			if(((p.ticks*p.speed)*p.ticks/10 + -yOffset/5) > sr.getScaledHeight() || ((p.ticks*p.speed)*p.ticks/10 + -yOffset/5) < 0 || (p.x + Math.sin(p.ticks/2)*50 + -xOffset/5) > sr.getScaledWidth()|| (p.x + Math.sin(p.ticks/2)*50 + -xOffset/5) < 0){
				toremove.add(p);
			}
		}
		
		array.removeAll(toremove);
		GlStateManager.color(1, 1, 1, 1);
		GL11.glColor4f(1, 1, 1, 1);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}

}
