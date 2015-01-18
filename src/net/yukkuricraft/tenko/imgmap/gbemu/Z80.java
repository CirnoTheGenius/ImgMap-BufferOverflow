package net.yukkuricraft.tenko.imgmap.gbemu;

import java.util.logging.Logger;

// GB Z80 MPU/CPU/Core, however you want to call it.
// Based off of https://github.com/Two9A/jsGB/blob/master/js/z80.js
// Loosely translated from JS to Java. Not expected to work or be functioning.
public class Z80 {

	private static final Logger logger = Logger.getLogger("ImgMap");

	// Registers
	private int a = 0, b = 0, c = 0, d = 0, e = 0, h = 0, l = 0, f = 0; 	// 8-bit
	private int sp = 0, pc = 0; 											// 16-bit
	private int i = 0, r = 0;												// ???
	private int m = 0, t = 0; 												// Clock

	// Clock (GB emulation is confusing.)
	private int clock_m = 0;

	// Memory interface. Read: RAM manager.
	private final MMU MMU = new MMU(); // Yes, the variable name is bad.

	public void resetEmulation(){
		a = 0; b = 0; c = 0; d = 0; e = 0; h = 0; l = 0; f = 0;
		sp = 0; pc = 0;
		m = 0;
		logger.info("[Z80] Reset emulator.");
	}

	public void tick(){
		r = (r + 1) & 128;
		//opcodeMap[pc++].execute();
		// use lookup map to execute command with "pc++".
		pc &= 65535;
		clock_m += m;
	}


	// TODO: Rewrite this to be a hashmap so I can put it into the same format as
	// TODO: Z80._map on the JS page.
	// Memory management hell.
	private Opcode[] opcodeMap = {

			// LDrr_bb
			new Opcode() {
				@Override
				public void execute() {
					//self assignment of b (wat.)
					m = 1;
				}
			},

			//LDrr_bc
			new Opcode() {
				@Override
				public void execute() {
					b = c;
					m = 1;
				}
			},

			//LDrr_bd
			new Opcode() {
				@Override
				public void execute() {
					b = d;
					m = 1;
				}
			},

			//LDrr_be
			new Opcode() {
				@Override
				public void execute() {
					b = e;
					m = 1;
				}
			},

			//LDrr_bh
			new Opcode() {
				@Override
				public void execute() {
					b = h;
					m = 1;
				}
			},

			//LDrr_bl
			new Opcode() {
				@Override
				public void execute() {
					b = l; // oh god this is confusing; is it a 1 or a L. they look so similar.
					m = 1;
				}
			},

			//LDrr_ba
			new Opcode() {
				@Override
				public void execute() {
					b = a;
					m = 1;
				}
			},	
			
			// LDrr_cb
			new Opcode() {
				@Override
				public void execute() {
					c = b;
					m = 1;
				}
			},

			//LDrr_cc
			new Opcode() {
				@Override
				public void execute() {
					// self assignment to c
					m = 1;
				}
			},

			//LDrr_cd
			new Opcode() {
				@Override
				public void execute() {
					c = d;
					m = 1;
				}
			},

			//LDrr_ce
			new Opcode() {
				@Override
				public void execute() {
					c = e;
					m = 1;
				}
			},

			//LDrr_ch
			new Opcode() {
				@Override
				public void execute() {
					c = h;
					m = 1;
				}
			},

			//LDrr_cl
			new Opcode() {
				@Override
				public void execute() {
					c = l;
					m = 1;
				}
			},

			//LDrr_ca
			new Opcode() {
				@Override
				public void execute() {
					c = a;
					m = 1;
				}
			},
						
			// LDrr_db
			new Opcode() {
				@Override
				public void execute() {
					d = b;
					m = 1;
				}
			},

			//LDrr_dc
			new Opcode() {
				@Override
				public void execute() {
					d = c;
					m = 1;
				}
			},

			//LDrr_dd
			new Opcode() {
				@Override
				public void execute() {
					// self assignment to d
					m = 1;
				}
			},

			//LDrr_de
			new Opcode() {
				public void execute() {
					d = e;
					m = 1;
				}
			},

			//LDrr_dh
			new Opcode() {
				@Override
				public void execute() {
					d = h;
					m = 1;
				}
			},

			//LDrr_dl
			new Opcode() {
				@Override
				public void execute() {
					d = l;
					m = 1;
				}
			},

			//LDrr_da
			new Opcode() {
				@Override
				public void execute() {
					e = a;
					m = 1;
				}
			},
			
			// LDrr_eb
			new Opcode() {
				@Override
				public void execute() {
					e = b;
					m = 1;
				}
			},

			//LDrr_ec
			new Opcode() {
				@Override
				public void execute() {
					e = c;
					m = 1;
				}
			},

			//LDrr_ed
			new Opcode() {
				@Override
				public void execute() {
					e = d;
					m = 1;
				}
			},

			//LDrr_ee
			new Opcode() {
				@Override
				public void execute() {
					// self assingment to e
					m = 1;
				}
			},

			//LDrr_eh
			new Opcode() {
				@Override
				public void execute() {
					e = h;
					m = 1;
				}
			},

			//LDrr_el
			new Opcode() {
				@Override
				public void execute() {
					e = l;
					m = 1;
				}
			},

			//LDrr_ea
			new Opcode() {
				@Override
				public void execute() {
					e = a;
					m = 1;
				}
			},
			
			// LDrr_hb
			new Opcode() {
				@Override
				public void execute() {
					h = b;
					m = 1;
				}
			},

			//LDrr_hc
			new Opcode() {
				@Override
				public void execute() {
					h = c;
					m = 1;
				}
			},

			//LDrr_hd
			new Opcode() {
				@Override
				public void execute() {
					h = d;
					m = 1;
				}
			},

			//LDrr_he
			new Opcode() {
				@Override
				public void execute() {
					h = e;
					m = 1;
				}
			},

			//LDrr_hh
			new Opcode() {
				@Override
				public void execute() {
					// self-assignment to h
					m = 1;
				}
			},

			//LDrr_hl
			new Opcode() {
				@Override
				public void execute() {
					h = l;
					m = 1;
				}
			},

			//LDrr_ha
			new Opcode() {
				@Override
				public void execute() {
					h = a;
					m = 1;
				}
			},
			
			// LDrr_lb
			new Opcode() {
				@Override
				public void execute() {
					l = b;
					m = 1;
				}
			},

			//LDrr_lc
			new Opcode() {
				@Override
				public void execute() {
					l = c;
					m = 1;
				}
			},

			//LDrr_ld
			new Opcode() {
				@Override
				public void execute() {
					l = d;
					m = 1;
				}
			},

			//LDrr_le
			new Opcode() {
				@Override
				public void execute() {
					l = e;
					m = 1;
				}
			},

			//LDrr_lh
			new Opcode() {
				@Override
				public void execute() {
					l = h;
					m = 1;
				}
			},

			//LDrr_ll
			new Opcode() {
				@Override
				public void execute() {
					// self assignment
					m = 1;
				}
			},

			//LDrr_la
			new Opcode() {
				@Override
				public void execute() {
					l = a;
					m = 1;
				}
			},

			// LDrr_ab
			new Opcode() {
				@Override
				public void execute() {
					a = b;
					m = 1;
				}
			},

			//LDrr_ac
			new Opcode() {
				@Override
				public void execute() {
					a = c;
					m = 1;
				}
			},

			//LDrr_ad
			new Opcode() {
				@Override
				public void execute() {
					a = d;
					m = 1;
				}
			},

			//LDrr_ae
			new Opcode() {
				@Override
				public void execute() {
					a = e;
					m = 1;
				}
			},

			//LDrr_ah
			new Opcode() {
				@Override
				public void execute() {
					a = h;
					m = 1;
				}
			},

			//LDrr_al
			new Opcode() {
				@Override
				public void execute() {
					a = l;
					m = 1;
				}
			},

			//LDrr_aa
			new Opcode() {
				@Override
				public void execute() {
					// self assignment
					m = 1;
				}
			},

			// LDrHLm_b
			new Opcode() {
				@Override
				public void execute() {
					b = MMU.readByte((h << 8) + l);
					m = 2;
				}
			},

			// LDrHLm_c
			new Opcode() {
				@Override
				public void execute() {
					c = MMU.readByte((h << 8) + l);
					m = 2;
				}
			},

			// LDrHLm_d
			new Opcode() {
				@Override
				public void execute() {
					d = MMU.readByte((h << 8) + l);
					m = 2;
				}
			},

			// LDrHLm_e
			new Opcode() {
				@Override
				public void execute() {
					e = MMU.readByte((h << 8) + l);
					m = 2;
				}
			},

			// LDrHLm_h
			new Opcode() {
				@Override
				public void execute() {
					h = MMU.readByte((h << 8) + l);
					m = 2;
				}
			},

			// LDrHLm_l
			new Opcode() {
				@Override
				public void execute() {
					l = MMU.readByte((h << 8) + l);
					m = 2;
				}
			},

			// LDrHLm_a
			new Opcode() {
				@Override
				public void execute() {
					a = MMU.readByte((h << 8) + l);
					m = 2;
				}
			},

			// LDHLmr_b
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((h << 8) + l, b);
					m = 2;
				}
			},

			// LDHLmr_c
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((h << 8) + l, c);
					m = 2;
				}
			},

			// LDHLmr_d
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((h << 8) + l, d);
					m = 2;
				}
			},

			// LDHLmr_e
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((h << 8) + l, e);
					m = 2;
				}
			},

			// LDHLmr_h
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((h << 8) + l, h);
					m = 2;
				}
			},

			// LDHLmr_l
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((h << 8) + l, l);
					m = 2;
				}
			},

			// LDHLmr_a
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((h << 8) + l, a);
					m = 2;
				}
			},

			// LDrn_b
			new Opcode() {
				@Override
				public void execute() {
					b = MMU.readByte(pc);
					pc++;
					m = 2;
				}
			},

			// LDrn_c
			new Opcode() {
				@Override
				public void execute() {
					c = MMU.readByte(pc);
					pc++;
					m = 2;
				}
			},

			// LDrn_d
			new Opcode() {
				@Override
				public void execute() {
					d = MMU.readByte(pc);
					pc++;
					m = 2;
				}
			},

			// LDrn_e
			new Opcode() {
				@Override
				public void execute() {
					e = MMU.readByte(pc);
					pc++;
					m = 2;
				}
			},

			// LDrn_h
			new Opcode() {
				@Override
				public void execute() {
					h = MMU.readByte(pc);
					pc++;
					m = 2;
				}
			},

			// LDrn_l
			new Opcode() {
				@Override
				public void execute() {
					l = MMU.readByte(pc);
					pc++;
					m = 2;
				}
			},

			// LDrn_a
			new Opcode() {
				@Override
				public void execute() {
					a = MMU.readByte(pc);
					pc++;
					m = 2;
				}
			},

			// LDHLmn
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((h << 8) + l, MMU.readByte(pc));
					pc++;
					m = 3;
				}
			},

			//LDBCmA
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((b << 8) + c, a);
					m = 2;
				}
			},

			//LDDEmA
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((d << 8) + e, a);
					m = 2;
				}
			},

			//LDmmA
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte(MMU.readWord(pc), a);
					pc += 2;
					m = 4;
				}
			},

			//LDABCm
			new Opcode() {
				@Override
				public void execute() {
					a = MMU.readByte((b << 8) + c);
					m = 2;
				}
			},

			//LDADEm
			new Opcode() {
				@Override
				public void execute() {
					a = MMU.readByte((d << 8) + e);
					m = 2;
				}
			},

			//LDAmm
			new Opcode() {
				@Override
				public void execute() {
					a = MMU.readByte(MMU.readWord(pc));
					pc += 2;
					m = 4;
				}
			},

			//LDBCnn
			new Opcode() {
				@Override
				public void execute() {
					c = MMU.readByte(pc);
					b = MMU.readByte(pc+1);
					pc += 2;
					m = 3;
				}
			},

			//LDDEnn
			new Opcode() {
				@Override
				public void execute() {
					e = MMU.readByte(pc);
					d = MMU.readByte(pc+1);
					pc += 2;
					m = 3;
				}
			},

			//LDHLnn
			new Opcode() {
				@Override
				public void execute() {
					l = MMU.readByte(pc);
					h = MMU.readByte(pc+1);
					pc += 2;
					m = 3;
				}
			},

			//LDSPnn
			new Opcode() {
				@Override
				public void execute() {
					sp = MMU.readWord(pc);
					pc += 2;
					m = 3;
				}
			},

			//LDHLmm
			new Opcode() {
				@Override
				public void execute() {
					int i = MMU.readWord(pc);
					pc += 2;
					l = MMU.readByte(i);
					h = MMU.readByte(i+1);
					m = 5;
				}
			},

			//LDmmHL
			new Opcode() {
				@Override
				public void execute() {
					int i = MMU.readWord(pc);
					pc += 2;
					MMU.writeWord(i, (h << 8)+l);
					m = 5;
				}
			},

			//LDHLIA
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte((h << 8) + l, a);
					l = (l + 1) & 255;
					if(l == 0){ // if(!Z80._r.l)
						h = (h + 1) & 255;
					}
					m = 2;
				}
			},

			//LDAHLI
			new Opcode() {
				@Override
				public void execute() {
					a = MMU.readByte((h << 8) + l);
					l = (l + 1) & 255;
					if(l == 0){
						h = (h + 1) & 255;
					}
					m = 2;
				}
			},

			//LDAIOn
			new Opcode() {
				@Override
				public void execute() {
					a = MMU.readByte(0xFF00 + MMU.readByte(pc));
					pc++;
					m = 3;
				}
			},

			//LDIOnA
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte(0xFF00 + MMU.readByte(pc), a);
					pc++;
					m = 3;
				}
			},

			//LDAIOC
			new Opcode() {
				@Override
				public void execute() {
					a = MMU.readByte(0xFF00 + c);
					m = 2;
				}
			},

			//LDIOCA
			new Opcode() {
				@Override
				public void execute() {
					MMU.writeByte(0xFF00 + c, a);
					m = 2;
				}
			},

			//LDHLSPn
			new Opcode() {
				@Override
				public void execute() {
					int i = MMU.readByte(pc);
					if(i > 127){
						i = -((~i + 1) & 255);
					}
					pc++;
					i += sp;
					h = (i >> 8) & 255;
					l = i & 255;
					m = 3;
				}
			},

			//SWAPr_b
			new Opcode() {
				@Override
				public void execute() {
					int tr = b;
					b = ((tr & 0xF) << 4)|((tr & 0xF0) >> 4);
					f = (b != 0) ? 0 : 0x80;
					m = 1;
				}
			},

			//SWAPr_c
			new Opcode() {
				@Override
				public void execute() {
					int tr = c;
					c = ((tr & 0xF) << 4) | ((tr & 0xF0) >> 4);
					f = (c != 0) ? 0 : 0x80;
					m = 1;
				}
			},

			//SWAPr_d
			new Opcode() {
				@Override
				public void execute() {
					int tr = d;
					d = ((tr & 0xF) << 4) | ((tr & 0xF0) >> 4);
					f = (d != 0) ? 0 : 0x80;
					m = 1;
				}
			},

			//SWAPr_e
			new Opcode() {
				@Override
				public void execute() {
					int tr = e;
					e = ((tr & 0xF) << 4) | ((tr & 0xF0) >> 4);
					f = (e != 0) ? 0 : 0x80;
					m = 1;
				}
			},

			//SWAPr_h
			new Opcode() {
				@Override
				public void execute() {
					int tr = h;
					h = ((tr & 0xF) << 4) | ((tr & 0xF0) >> 4);
					f = (h != 0) ? 0 : 0x80;
					m = 1;
				}
			},

			//SWAPr_l
			new Opcode() {
				@Override
				public void execute() {
					int tr = l;
					l = ((tr & 0xF) << 4) | ((tr & 0xF0) >> 4);
					f = (l != 0) ? 0 : 0x80;
					m = 1;
				}
			},

			//SWAPr_a
			new Opcode() {
				@Override
				public void execute() {
					int tr = a;
					a = ((tr & 0xF) << 4) | ((tr & 0xF0) >> 4);
					f = (a != 0) ? 0 : 0x80;
					m = 1;
				}
			},

			//ADDr_b
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += b;
					f = (a >> 255) != 0 ? 0x10 : 0;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a^b^q) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//ADDr_c
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += c;
					f = (a >> 255) != 0 ? 0x10 : 0;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a^c^q) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//ADDr_d
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += d;
					f = (a >> 255) != 0 ? 0x10 : 0;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a^d^q) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//ADDr_e
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += e;
					f = (a >> 255) != 0 ? 0x10 : 0;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a^e^q) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//ADDr_h
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += h;
					f = (a >> 255) != 0 ? 0x10 : 0;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a^h^q) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//ADDr_l
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += l;
					f = (a >> 255) != 0 ? 0x10 : 0;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a^l^q) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//ADDr_a
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += a;
					f = (a >> 255) != 0 ? 0x10 : 0;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a^a^q) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//ADDHL
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					int w = MMU.readByte((h << 8) + l);
					a += w;
					f = (a > 255) ? 0x10 : 0;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}
					if(((a ^ q ^ w) & 0x10) != 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//ADDn
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					int w = MMU.readByte(pc);
					a += w;
					f = (a > 255) ? 0x10 : 0;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}
					if(((a ^ q ^ w) & 0x10) != 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//ADDHLBC
			new Opcode() {
				@Override
				public void execute() {
					int hl = (h << 8) + l;
					hl += (b << 8) + c;
					if(hl > 65535){
						f |= 0x10;
					} else {
						f &= 0xEF;
					}

					h = (hl >> 8) & 255;
					l = hl & 255;
					m = 3;
				}
			},

			//ADDHLDE
			new Opcode() {
				@Override
				public void execute() {
					int hl = (h << 8) + l;
					hl += (d << 8) + e;
					if(hl > 65535){
						f |= 0x10;
					} else {
						f &= 0xEF;
					}

					h = (hl >> 8) & 255;
					l = hl & 255;
					m = 3;
				}
			},

			//ADDHLHL
			new Opcode() {
				@Override
				public void execute() {
					int hl = (h << 8) + l;
					hl += (h << 8) + l;
					if(hl > 65535){
						f |= 0x10;
					} else {
						f &= 0xEF;
					}

					h = (hl >> 8) & 255;
					l = hl & 255;
					m = 3;
				}
			},

			//ADDHLS
			new Opcode() {
				@Override
				public void execute() {
					int hl = (h << 8) + l;
					hl += sp;
					if(hl > 65535){
						f |= 0x10;
					} else {
						f &= 0xEF;
					}

					h = (hl >> 8) & 255;
					l = hl & 255;
					m = 3;
				}
			},

			//ADDSPn
			new Opcode() {
				@Override
				public void execute() {
					int i = MMU.readByte(pc);
					if(i > 127){
						i = -((~i + 1) & 255);
					}

					pc++;
					sp += i;
					m = 4;
				}
			},

			//ADCr_b
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += b;
					a += ((f & 0x10) != 0) ? 1 : 0;
					f = (a > 255) ? 0x10 : 0;
					a &= 255;
					f |= 0x80;
					if(((a ^ b ^ q) & 0x10) != 0){
						f |= 0x20;
					}
					m = 1;
				}
			},

			//ADCr_c
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += c;
					a += ((f & 0x10) != 0) ? 1 : 0;
					f = (a > 255) ? 0x10 : 0;
					a &= 255;
					f |= 0x80;
					if(((a ^ c ^ q) & 0x10) != 0){
						f |= 0x20;
					}
					m = 1;
				}
			},

			//ADCr_d
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += d;
					a += ((f & 0x10) != 0) ? 1 : 0;
					f = (a > 255) ? 0x10 : 0;
					a &= 255;
					f |= 0x80;
					if(((a ^ d ^ q) & 0x10) != 0){
						f |= 0x20;
					}
					m = 1;
				}
			},

			//ADCr_e
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += e;
					a += ((f & 0x10) != 0) ? 1 : 0;
					f = (a > 255) ? 0x10 : 0;
					a &= 255;
					f |= 0x80;
					if(((a ^ e ^ q) & 0x10) != 0){
						f |= 0x20;
					}
					m = 1;
				}
			},

			//ADCr_h
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += h;
					a += ((f & 0x10) != 0) ? 1 : 0;
					f = (a > 255) ? 0x10 : 0;
					a &= 255;
					f |= 0x80;
					if(((a ^ h ^ q) & 0x10) != 0){
						f |= 0x20;
					}
					m = 1;
				}
			},

			//ADCr_l
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += l;
					a += ((f & 0x10) != 0) ? 1 : 0;
					f = (a > 255) ? 0x10 : 0;
					a &= 255;
					f |= 0x80;
					if(((a ^ l ^ q) & 0x10) != 0){
						f |= 0x20;
					}
					m = 1;
				}
			},

			//ADCr_a
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a += a;
					a += ((f & 0x10) != 0) ? 1 : 0;
					f = (a > 255) ? 0x10 : 0;
					a &= 255;
					f |= 0x80;
					if(((a ^ a ^ q) & 0x10) != 0){
						f |= 0x20;
					}
					m = 1;
				}
			},

			//ADCHL
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					int w = MMU.readByte((h << 8) + l);

					a += w;
					a += ((f & 0x10) != 0) ? 1 : 0;
					f = (a > 255) ? 0x10 : 0;
					r &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ w ^ q) & 0x10) != 0){
						f |= 0x20;
					}

					m = 2;
				}
			},

			//ADCn
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					int w = MMU.readByte(pc);

					a += w;
					pc++;
					a += ((f & 0x10) != 0) ? 1 : 0;
					f = (a > 255) ? 0x10 : 0;
					r &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ w ^ q) & 0x10) != 0){
						f |= 0x20;
					}

					m = 2;
				}
			},

			//SUBr_b
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= b;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;

					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ b ^ q) & 0x20) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//SUBr_b
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= c;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;

					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ c ^ q) & 0x20) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//SUBr_d
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= d;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;

					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ d ^ q) & 0x20) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//SUBr_e
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= e;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;

					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ e ^ q) & 0x20) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//SUBr_h
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= h;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;

					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ h ^ q) & 0x20) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//SUBr_l
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= l;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;

					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ l ^ q) & 0x20) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//SUBr_a
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= a;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;

					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ a ^ q) & 0x20) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//SUBHL
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					int w = MMU.readByte((h << 8) + l);
					a -= w;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ w ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SUBn
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					int w = MMU.readByte(pc);
					a -= w;
					pc++;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ w ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SBCr_b
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= b;
					a -= ((f & 0x10) != 0) ? 1 : 0;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ b ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SBCr_c
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= c;
					a -= ((f & 0x10) != 0) ? 1 : 0;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ c ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SBCr_d
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= d;
					a -= ((f & 0x10) != 0) ? 1 : 0;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ d ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SBCr_e
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= e;
					a -= ((f & 0x10) != 0) ? 1 : 0;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ e ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SBCr_h
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= h;
					a -= ((f & 0x10) != 0) ? 1 : 0;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ h ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SBCr_l
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= l;
					a -= ((f & 0x10) != 0) ? 1 : 0;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ l ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SBCr_a
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					a -= a;
					a -= ((f & 0x10) != 0) ? 1 : 0;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ a ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SBCHL
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					int w = MMU.readByte((h << 8) + l);
					a -= w;
					a -= ((f & 0x10) != 0) ? 1 : 0;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ w ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//SBCn
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					int w = MMU.readByte(pc);
					a -= w;
					pc++;
					a -= ((f & 0x10) != 0) ? 1 : 0;
					f = (a < 0) ? 0x50 : 0x40;
					a &= 255;
					if(a == 0){
						f |= 0x80;
					}

					if(((a ^ w ^ q) & 0x10) == 0){
						f |= 0x20;
					}
					m = 2;
				}
			},

			//CPr_b
			new Opcode() {
				@Override
				public void execute() {
					int i = a;
					i -= b;
					f = (i < 0) ? 0x50 : 0x40;
					i &= 255;
					if(i == 0){
						f |= 0x80;
					}

					if(((a ^ b ^ i) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//CPr_c
			new Opcode() {
				@Override
				public void execute() {
					int i = a;
					i -= c;
					f = (i < 0) ? 0x50 : 0x40;
					i &= 255;
					if(i == 0){
						f |= 0x80;
					}

					if(((a ^ c ^ i) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//CPr_d
			new Opcode() {
				@Override
				public void execute() {
					int i = a;
					i -= d;
					f = (i < 0) ? 0x50 : 0x40;
					i &= 255;
					if(i == 0){
						f |= 0x80;
					}

					if(((a ^ d ^ i) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//CPr_e
			new Opcode() {
				@Override
				public void execute() {
					int i = a;
					i -= e;
					f = (i < 0) ? 0x50 : 0x40;
					i &= 255;
					if(i == 0){
						f |= 0x80;
					}

					if(((a ^ e ^ i) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//CPr_h
			new Opcode() {
				@Override
				public void execute() {
					int i = a;
					i -= h;
					f = (i < 0) ? 0x50 : 0x40;
					i &= 255;
					if(i == 0){
						f |= 0x80;
					}

					if(((a ^ h ^ i) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//CPr_l
			new Opcode() {
				@Override
				public void execute() {
					int i = a;
					i -= l;
					f = (i < 0) ? 0x50 : 0x40;
					i &= 255;
					if(i == 0){
						f |= 0x80;
					}

					if(((a ^ l ^ i) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//CPr_a
			new Opcode() {
				@Override
				public void execute() {
					int i = a;
					i -= a;
					f = (i < 0) ? 0x50 : 0x40;
					i &= 255;
					if(i == 0){
						f |= 0x80;
					}

					if(((a ^ a ^ i) & 0x10) != 0){
						f |= 0x20;
					}

					m = 1;
				}
			},

			//CPHL
			new Opcode() {
				@Override
				public void execute() {
					int i = a;
					int w = MMU.readByte((h << 8) + l);
					i -= w;
					f = (i < 0) ? 0x50 : 0x40;
					i &= 255;
					if(i == 0){
						f |= 0x80;
					}

					if(((a ^ i ^ w) & 0x10) != 0){
						f |= 0x20;
					}

					m = 2;
				}
			},

			//CPn
			new Opcode() {
				@Override
				public void execute() {
					int i = a;
					int w = MMU.readByte(pc);
					i -= w;
					pc++;
					f = (i < 0) ? 0x50 : 0x40;
					i &= 255;
					if(i == 0){
						f |= 0x80;
					}

					if(((a ^ i ^ w) & 0x10) != 0){
						f |= 0x20;
					}

					m = 2;
				}
			},

			//DAA
			new Opcode() {
				@Override
				public void execute() {
					int q = a;
					if((f & 0x20) != 0 || ((a & 15) > 9)){
						a += 6;
					}

					f &= 0xEF;

					if((f & 0x20) != 0 || (q > 0x99)){
						a += 0x60;
						f |= 0x10;
					}
					m = 1;
				}
			},

			

	};



}