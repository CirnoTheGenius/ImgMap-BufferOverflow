package net.yukkuricraft.tenko.imgmap.gbemu;

public class Z80_EXP {

	protected IntegerCapsule a, b, c, d, e, h, l, f;
	protected IntegerCapsule sp, pc, i, r, m, ime;

	public Z80_EXP(){

	}

	protected class IntegerCapsule {

		private int theInt;

		public int get(){
			return theInt;
		}

		public void set(int newInt){
			this.theInt = newInt;
		}

	}


}