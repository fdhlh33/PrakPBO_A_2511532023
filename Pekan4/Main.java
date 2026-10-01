package Pekan4;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		ArrayList<Rekening> daftarRekening = new ArrayList<>();
		Rekening akunAktif = null; // Objek belum diinisialisasi (null)
		boolean isRunning = true;
		
		System.out.println("=== SISTEM PERBANKAN MINI ===");
		
		while (isRunning) {
			System.out.println("\nMenu Utama:");
			System.out.println("1. Buka Rekening Baru");
			System.out.println("2. Setor Tunai");
			System.out.println("3. Tarik Tunai");
			System.out.println("4. Cek Informasi Rekening");
			System.out.println("5. Ganti / Pilih Akun Aktif");
			System.out.println("6. Cetak Mutasi (Riwayat)");
			System.out.println("7. Simulasi Akhir Bulan (Khusus Tabungan)");
			System.out.println("8. Cetak 3 Transaksi Terbaru");
			System.out.println("0. Keluar");
			
			// Menampilkan status akun yang sedang aktif
			if (akunAktif != null) {
				System.out.println("[Akun Aktif: " + akunAktif.getNamaPemilik() + " (" + akunAktif.getNomorRekening() + ")]");
			} else {
				System.out.println("[Akun Aktif: Belum ada]");
			}
			
			System.out.print("Pilih Menu: ");
			int pilihan = input.nextInt();
			input.nextLine(); // Membersihkan buffer enter
			
			switch (pilihan) {
			case 1:
			    System.out.print("Masukkan No Rekening: ");
			    String no = input.nextLine();
			    System.out.print("Masukkan Nama Pemilik: ");
			    String nama = input.nextLine();
			    System.out.print("Masukkan Saldo Awal: ");
			    double saldo = input.nextDouble();
			    input.nextLine();
			    
			    String pin;
			    while (true) {
			        System.out.print("Masukkan PIN Awal (6 digit angka): ");
			        pin = input.nextLine();
			        if (pin.matches("\\d{6}")) {
			            break;
			        }
			        System.out.println("Gagal: PIN harus terdiri dari tepat 6 digit angka!");
			    }
			    
			    System.out.println("\nPilih Produk: ");
			    System.out.println("1. Tabungan Umum");
			    System.out.println("2. Giro Bisnis");
			    System.out.print("Pilihan Produk: ");
			    int produk = input.nextInt();
			    input.nextLine();
			    
			    Rekening akunBaru;
			    
			    if(produk == 1) {
			    	System.out.print("Masukkan Suku Bunga (%): ");
			    	double sukuBunga = input.nextDouble();
			    	input.nextLine();
			    	
			    	akunBaru = new RekeningTabungan(no, nama, saldo, pin, sukuBunga);
			    } else if (produk == 2){
			    	System.out.print("Masukkan Batas Overdraft");
			    	double batasOverdraft = input.nextDouble();
			    	input.nextLine();
			    	
			    	akunBaru = new RekeningGiro(no, nama, saldo, pin, batasOverdraft);
			    } else {
			    	System.out.println("Pilihan produk tidak valid!");
			    	break;
			    }
			    
			    daftarRekening.add(akunBaru);
			    akunAktif = akunBaru;
			    break;
			
			case 2:
				if (akunAktif == null) {
					System.out.println("Error: Mohon maaf, Anda belum memiliki nomor rekening!");
				} else {
					System.out.print("Masukkan nominal setor: ");
					double setor = input.nextDouble();
					akunAktif.setorTunai(setor); // Memanggil Behavior / method
				}
				break;
				
			case 3:
			    if (akunAktif == null) {
			        System.out.println("Error: Mohon maaf, Anda belum memiliki nomor rekening!");
			    } else {
			        System.out.print("Masukkan PIN: ");
			        String pinTarik = input.nextLine();

			        if (akunAktif.otentikasi(pinTarik)) {
			            System.out.print("Masukkan nominal tarik tunai: ");
			            double tarik = input.nextDouble();
			            akunAktif.tarikTunai(tarik);
			        } else {
			            System.out.println("Akses ditolak: PIN yang anda masukkan salah!");
			        }
			    }
			    break;
				
			case 4:
				if (akunAktif == null) {
					System.out.println("Error: Anda belum membuka rekening!");
				} else {
					akunAktif.cekInformasi();
				}
				break;
				
			case 5:
				if (daftarRekening.isEmpty()) {
					System.out.println("Error: Belum ada rekening terdaftar di sistem!");
				} else {
					System.out.print("Masukkan No. Rekening yang dicari: ");
					String cariNo = input.nextLine();
					boolean ditemukan = false;

					for (Rekening r : daftarRekening) {
						if (r.getNomorRekening().equalsIgnoreCase(cariNo)) {
							akunAktif = r;
							ditemukan = true;
							System.out.println("Berhasil beralih ke akun milik: " + r.getNamaPemilik());
							break;
						}
					}

					if (!ditemukan) {
						System.out.println("Error: Nomor rekening tidak ditemukan!");
					}
				}
				break;
				
			case 6:
			    if (akunAktif == null) {
			        System.out.println("Error: Anda belum membuka rekening");
			    } else {
			    	System.out.print("Masukkan PIN: ");
					String pinMutasi = input.nextLine();
					
					if (akunAktif.otentikasi(pinMutasi)) {
						akunAktif.cetakMutasi();
					} else {
						System.out.println("Akses ditolak: PIN yang anda masukkan salah!");
					}
			    }
			    break;
				
			case 7:
				if(akunAktif == null) {
					System.out.println("Error: Anda belum membuka rekening");
				} else if (akunAktif instanceof RekeningTabungan){
					RekeningTabungan tabungan = (RekeningTabungan) akunAktif;
					tabungan.tambahBungaAkhirBulan();
				} else {
					System.out.println("Gagal: Fitur bunga akhir bulan hanya berlaku untuk Rekening Tabungan");
				}
				break;
				
			case 8:
				if(akunAktif == null) {
					System.out.println("Error: Anda belum membuka rekening");
				} else {
					akunAktif.cetakTigaTransaksiTerbaru();
				}
				break;
				
			case 0:
				isRunning = false;
				System.out.println("Sistem ditutup. Terima kasih!");
				break;
				
			default:
				System.out.println("Pilihan tidak valid!");
			}
		}
		input.close();
	}
}