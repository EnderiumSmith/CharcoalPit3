package charcoalPit.entity;

import charcoalPit.CharcoalPit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

public class AirplaneModel<T extends Airplane> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CharcoalPit.MODID, "airplane"), "main");
	private final ModelPart airplane;
	private final ModelPart airplane_nested;
	private final ModelPart wings;
	private final ModelPart aileron_left,aileron_right;
	private final ModelPart tail;
	private final ModelPart elevator;
	private final ModelPart rudder;
	private final ModelPart rudder_left,rudder_right;
	private final ModelPart prop;
	
	public AirplaneModel(ModelPart root) {
		this.airplane = root.getChild("airplane");
		this.airplane_nested=airplane.getChild("airplane_nested");
		this.wings=airplane_nested.getChild("wings");
		this.aileron_left=wings.getChild("aileron_left_r1");
		this.aileron_right=wings.getChild("aileron_right_r1");
		this.tail=wings.getChild("tail");
		this.elevator=tail.getChild("elevator_r1");
		this.rudder=tail.getChild("rudder");
		this.rudder_left=rudder.getChild("rudder_left_r1");
		this.rudder_right=rudder.getChild("rudder_right_r1");
		this.prop=airplane_nested.getChild("engine").getChild("prop");
	}
	
	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		
		PartDefinition airplane = partdefinition.addOrReplaceChild("airplane", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		PartDefinition airplane_nested = airplane.addOrReplaceChild("airplane_nested", CubeListBuilder.create(), PartPose.offset(0.0F, -28.0F, -8.0F));
		
		PartDefinition wings = airplane_nested.addOrReplaceChild("wings", CubeListBuilder.create().texOffs(0, 0).addBox(-64.0F, -1.0F, -10.0F, 128.0F, 1.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -11.0F, 7.0F, -0.0873F, 0.0F, 0.0F));
		
		PartDefinition aileron_right_r1 = wings.addOrReplaceChild("aileron_right_r1", CubeListBuilder.create().texOffs(29, 48).addBox(-12.0F, 0.0F, 0.0F, 24.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-52.0F, -0.5F, 10.0F, 0F, 0.0F, 0.0F));
		
		PartDefinition aileron_left_r1 = wings.addOrReplaceChild("aileron_left_r1", CubeListBuilder.create().texOffs(29, 48).addBox(-12.0F, 0.0F, 0.0F, 24.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(52.0F, -0.5F, 10.0F, 0F, 0.0F, 0.0F));
		
		PartDefinition tail = wings.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 27).addBox(-17.0F, 0.0F, 47.0F, 32.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 0.0F, 0.0F));
		
		PartDefinition boom_left_r1 = tail.addOrReplaceChild("boom_left_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-33.0F, -2.5F, -1.0F, 64.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.0F, 2.0F, 22.0F, 0.0F, 1.5708F, 0.0F));
		
		PartDefinition boom_right_r1 = tail.addOrReplaceChild("boom_right_r1", CubeListBuilder.create().texOffs(0, 22).addBox(-33.0F, -2.5F, -1.0F, 64.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-18.0F, 2.0F, 22.0F, 0.0F, 1.5708F, 0.0F));
		
		PartDefinition elevator_r1 = tail.addOrReplaceChild("elevator_r1", CubeListBuilder.create().texOffs(29, 40).addBox(-16.0F, 0.0F, 0.0F, 32.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.5F, 55.0F, 0F, 0.0F, 0.0F));
		
		PartDefinition rudder = tail.addOrReplaceChild("rudder", CubeListBuilder.create().texOffs(0, 40).addBox(16.5F, -8.75F, -5.25F, 1.0F, 17.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 40).addBox(-17.5F, -8.75F, -5.25F, 1.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 9.75F, 53.25F, 0.0873F, 0.0F, 0.0F));
		
		PartDefinition rudder_right_r1 = rudder.addOrReplaceChild("rudder_right_r1", CubeListBuilder.create().texOffs(15, 39).addBox(0.0F, -8.0F, 0.0F, 0.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.0F, 0.25F, 0.75F, 0.0F, -0.2618F, 0.0F));
		
		PartDefinition rudder_left_r1 = rudder.addOrReplaceChild("rudder_left_r1", CubeListBuilder.create().texOffs(15, 39).addBox(0.0F, -8.0F, 0.0F, 0.0F, 16.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0F, 0.25F, 0.75F, 0.0F, -0.2618F, 0.0F));
		
		PartDefinition engine = airplane_nested.addOrReplaceChild("engine", CubeListBuilder.create().texOffs(0, 78).addBox(-3.0F, -2.0F, -7.0F, 6.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(-8.0F, -1.5F, -2.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(-8.0F, -1.5F, -6.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(3.0F, -1.5F, -2.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 10).addBox(3.0F, -1.5F, -6.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 15.0F));
		
		PartDefinition fuel_line_r1 = engine.addOrReplaceChild("fuel_line_r1", CubeListBuilder.create().texOffs(67, 85).addBox(-0.5F, -7.0F, -0.5F, 1.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.0F, -6.5F, -0.1309F, 0.0F, 0.0F));
		
		PartDefinition prop = engine.addOrReplaceChild("prop", CubeListBuilder.create().texOffs(13, 0).addBox(-0.5F, -0.5F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));
		
		PartDefinition blade3_r1 = prop.addOrReplaceChild("blade3_r1", CubeListBuilder.create().texOffs(60, 85).addBox(-1.5F, -16.0F, -0.5F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, -0.3491F, 2.0944F));
		
		PartDefinition blade2_r1 = prop.addOrReplaceChild("blade2_r1", CubeListBuilder.create().texOffs(60, 85).addBox(-1.5F, -16.0F, -0.5F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, -0.3491F, -2.0944F));
		
		PartDefinition blade1_r1 = prop.addOrReplaceChild("blade1_r1", CubeListBuilder.create().texOffs(60, 85).addBox(-1.5F, -16.0F, -0.5F, 3.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.5F, 0.0F, -0.3491F, 0.0F));
		
		PartDefinition seat = airplane_nested.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(41, 66).addBox(-7.0F, -11.5F, 2.0F, 14.0F, 16.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(53, 55).addBox(-7.0F, 2.5F, -6.0F, 14.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.5F, 1.5F));
		
		PartDefinition stick = seat.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(90, 48).addBox(-0.5F, -10.0F, -0.5F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(73, 27).addBox(-4.5F, -11.0F, -0.5F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(3.5F, -15.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-4.5F, -15.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, -6.5F, 0.2618F, 0.0F, 0.0F));
		
		PartDefinition barrel = airplane_nested.addOrReplaceChild("barrel", CubeListBuilder.create().texOffs(33, 85).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 16.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(74, 66).addBox(5.0F, -8.0F, -5.0F, 1.0F, 16.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(33, 85).addBox(-6.0F, -8.0F, 5.0F, 12.0F, 16.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(74, 66).addBox(-6.0F, -8.0F, -5.0F, 1.0F, 16.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 66).addBox(-5.0F, 6.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
				.texOffs(0, 66).addBox(-5.0F, -7.0F, -5.0F, 10.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.0F, 11.5F));
		
		PartDefinition frame = airplane_nested.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 40).addBox(-1.0F, -6.25F, -7.0F, 2.0F, 1.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(29, 55).addBox(-6.0F, -6.0F, 9.5F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 28.0F, 1.0F));
		
		PartDefinition strut_front_left_r1 = frame.addOrReplaceChild("strut_front_left_r1", CubeListBuilder.create().texOffs(0, 37).addBox(0.0F, -0.5F, -0.5F, 37.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -5.5F, 10.0F, 0.0F, 0.3316F, -1.2392F));
		
		PartDefinition strut_left_r1 = frame.addOrReplaceChild("strut_left_r1", CubeListBuilder.create().texOffs(29, 45).addBox(0.0F, -0.5F, -0.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -5.5F, 11.0F, 0.0F, -0.1309F, -1.2392F));
		
		PartDefinition strut_front_right_r1 = frame.addOrReplaceChild("strut_front_right_r1", CubeListBuilder.create().texOffs(0, 37).addBox(-37.0F, -0.5F, -0.5F, 37.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -5.5F, 10.0F, 0.0F, -0.3316F, 1.2392F));
		
		PartDefinition strut_right_r1 = frame.addOrReplaceChild("strut_right_r1", CubeListBuilder.create().texOffs(29, 45).addBox(-34.0F, -0.5F, -0.5F, 34.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -5.5F, 11.0F, 0.0F, 0.1309F, 1.2392F));
		
		PartDefinition gear = airplane_nested.addOrReplaceChild("gear", CubeListBuilder.create().texOffs(0, 0).addBox(15.5F, -4.0F, 16.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-1.5F, -4.0F, -8.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-18.5F, -4.0F, 16.0F, 3.0F, 3.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(23, 78).addBox(16.0F, -3.0F, 17.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(23, 78).addBox(-18.0F, -3.0F, 17.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(23, 78).addBox(-1.0F, -3.0F, -7.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 28.0F, 1.0F));
		
		PartDefinition gear_strut_right_r1 = gear.addOrReplaceChild("gear_strut_right_r1", CubeListBuilder.create().texOffs(29, 59).addBox(-13.0F, -0.5F, -0.5F, 13.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -5.5F, 10.5F, 0.0F, 0.6545F, -0.3054F));
		
		PartDefinition gear_strut_left_r1 = gear.addOrReplaceChild("gear_strut_left_r1", CubeListBuilder.create().texOffs(29, 59).addBox(0.0F, -0.5F, -0.5F, 13.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -5.5F, 10.5F, 0.0F, -0.6545F, 0.3054F));
		
		return LayerDefinition.create(meshdefinition, 512, 128);
	}
	
	@Override
	public void setupAnim(T plane, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!plane.isOnGround()) {
			float f1 = (float)plane.getFallFlyingTicks();
			float f2 = Mth.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
			this.airplane_nested.xRot=(float)Math.toRadians(f2*headPitch);
			
			Vec3 vec3 = plane.getLookAngle();
			Vec3 vec31 = plane.getDeltaMovement();
			double d0 = vec31.horizontalDistanceSqr();
			double d1 = vec3.horizontalDistanceSqr();
			if (d0 > 0.0D && d1 > 0.0D) {
				double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
				double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
				float roll=-(float)(Math.signum(d3)*Math.acos(d2))*f2;
				this.airplane_nested.zRot=roll;
				this.aileron_right.xRot=-roll;
				this.aileron_left.xRot=roll;
				this.rudder_right.yRot=roll;
				this.rudder_left.yRot=roll;
			}
		}else{
			this.airplane_nested.xRot=0F;
			this.airplane_nested.zRot=0F;
			this.aileron_right.xRot=0F;
			this.aileron_left.xRot=0F;
			this.rudder_right.yRot=0F;
			this.rudder_left.yRot=0F;
		}
		if(plane.deltaEngine>0.1F) {
			plane.prop_travel += Math.toRadians(plane.deltaEngine);
		}
		this.prop.zRot = -plane.prop_travel;
	}
	
	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		airplane.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}
