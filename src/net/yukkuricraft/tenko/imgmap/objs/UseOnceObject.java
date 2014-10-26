package net.yukkuricraft.tenko.imgmap.objs;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * The name sounds silly; but the use of this class
 * is to give a hard reference to the object at first
 * and then give it a SoftReference as soon as it
 * is used once.
 *
 * This won't work for Reference objects for obvious
 * reasons.
 *
 */
public class UseOnceObject<T> {

	private Object subject;

	public UseOnceObject(Object object){
		subject = object;
	}

	public T get(){
		if(subject instanceof Reference){
			return ((Reference<T>)subject).get(); // Ewww... unsafe casting.
		} else {
			T oldSubject = (T)subject; // Establish a temporary hardlink. Ewww... more unsafe casting.
			subject = new SoftReference<T>((T)subject);
			return oldSubject;
		}
	}

}