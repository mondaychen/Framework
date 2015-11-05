package cs4620.splines.form;

import cs4620.common.BasicType;
import cs4620.mesh.MeshData;
import cs4620.mesh.gen.MeshGenOptions;
import cs4620.mesh.gen.MeshGenerator;
import cs4620.splines.SplineCurve;

public class MeshGenRevolution extends MeshGenerator {

	SplineCurve toRevolve;
	float scale;
	float sliceTolerance;
	
	@Override
	public void generate(MeshData outData, MeshGenOptions opt) {
		SplineCurve.build3DRevolution(toRevolve, outData, scale, sliceTolerance);
	}

	@Override
	public BasicType getType() {
		return null;
	}

	public void setSplineToRevolve(SplineCurve spline) {
		this.toRevolve= spline;
	}
	
	public void setScale(float scale) {
		this.scale= scale;
	}
	
	public void setSliceTolerance(float tolerance) {
		this.sliceTolerance = tolerance;
	}
}
