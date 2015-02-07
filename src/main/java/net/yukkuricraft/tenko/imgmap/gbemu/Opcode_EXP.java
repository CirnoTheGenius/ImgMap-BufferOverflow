package net.yukkuricraft.tenko.imgmap.gbemu;

public class Opcode_EXP {

	public Opcode_EXP(){

	}

	public static enum Register {
		A, B, C, D, E, H, L, F, SP, PC, I, R, M, IME;

		private Z80_EXP mpu;
		private Z80_EXP.IntegerCapsule z80Register;

		Register(){
			if(this.name().length() == 1){
				switch(this.name().charAt(0)){
					case 'A':
						z80Register = mpu.a;
						break;
					case 'B':
						z80Register = mpu.b;
				}
			}
		}

		public int get(){
			return z80Register.get();
		}

		public void set(int i){
			z80Register.set(i);
		}
	}

}