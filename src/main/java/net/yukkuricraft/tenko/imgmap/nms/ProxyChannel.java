package net.yukkuricraft.tenko.imgmap.nms;

public interface ProxyChannel {

	public void sendPacket(Object o);

	public void flush();

	public boolean isOpen();

//	public boolean isWritable(); I should probably leave this as a method, but isOpen and isWritable sounds redundent.

}