package charcoalPit.entity;

import charcoalPit.CharcoalPit;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class AirplaneRenderer<T extends Airplane> extends MobRenderer<T, AirplaneModel<T>> {
	
	public static final ResourceLocation AIRPLANE_TEXTURE=new ResourceLocation(CharcoalPit.MODID,"textures/entity/airplane.png");
	
	public AirplaneRenderer(EntityRendererProvider.Context context){
		super(context,new AirplaneModel<>(context.bakeLayer(AirplaneModel.LAYER_LOCATION)),0F);
	}
	
	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return AIRPLANE_TEXTURE;
	}
}
