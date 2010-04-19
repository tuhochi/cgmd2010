package at.ac.tuwien.cg.cgmd.bifth2010.level36;

public class Camera {
	
	private Matrix4x4 camMatrix;
	
	public Camera() {
		camMatrix = new Matrix4x4();
	}
    
	public void lookAt(float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float orientX, float orientY, float orientZ) {
		Vektor3 eye = new Vektor3();
		Vektor3 forward = new Vektor3();
		Vektor3 up = new Vektor3();
		Vektor3 side = new Vektor3();
		Vektor3 normEye = new Vektor3();
		Vektor3 normForward = new Vektor3();
		Vektor3 normSide = new Vektor3();
		Vektor3 normUp = new Vektor3();
		Matrix4x4 tmp = new Matrix4x4();
		
		eye.setX(eyeX);
		eye.setY(eyeY);
		eye.setZ(eyeZ);
		normEye.setVektor(eye.getNormalized());
	    
		forward.setX(centerX - eyeX);
		forward.setY(centerY - eyeY);
		forward.setZ(centerZ - eyeZ);
		normForward.setVektor(forward.getNormalized());
		System.out.println("Forward: " + normForward.toString());

	    up.setX(orientX);
	    up.setY(orientY);
	    up.setZ(orientZ);

	    side.setVektor(forward.getCrossed(up.getVektor()));
		normSide.setVektor(side.getNormalized());
		normUp.setVektor(normSide.getCrossed(normForward.getVektor()));
		System.out.println("Side: " + normSide.toString());
		System.out.println("Up: " + normUp.toString());

		tmp.setValue(0,0, normSide.getX());
		tmp.setValue(1,0, normUp.getX());
		tmp.setValue(2,0, -(normForward.getX()));
		tmp.setValue(3,0, 0.0f);
		tmp.setValue(0,1, normSide.getY());
		tmp.setValue(1,1, normUp.getY());
		tmp.setValue(1,2, -(normForward.getY()));
		tmp.setValue(1,3, 0.0f);
		tmp.setValue(0,2, normSide.getZ());
		tmp.setValue(1,2, normUp.getZ());
		tmp.setValue(2,2, -(normForward.getZ()));
		tmp.setValue(3,2, 0.0f);
		tmp.setValue(0,3, 0.0f);
		tmp.setValue(1,3, 0.0f);
		tmp.setValue(2,3, 0.0f);
		tmp.setValue(3,3, 1.0f); 
		
		tmp.printMatrix();
		
		camMatrix.loadIdentity();
		camMatrix.multiplyWith(tmp);
		camMatrix.translate(-normEye.getX(), -normEye.getY(), -normEye.getZ());
	}
	
	public Matrix4x4 getMatrix() {
		return this.camMatrix;
	}
}
