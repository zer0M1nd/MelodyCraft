package hciu.pub.mcmod.hciusutils.gui.render;

abstract public class AbstractTextureDrawer<T> {

	protected T obj;

	public AbstractTextureDrawer(T obj) {
		this.obj = obj;
	}

	public abstract void draw();

	public static <T1> AbstractTextureDrawer<T1> createEmpty(T1 obj) {
		return new AbstractTextureDrawer<T1>(obj) {
			@Override
			public void draw() {
			}
		};
	}

}
