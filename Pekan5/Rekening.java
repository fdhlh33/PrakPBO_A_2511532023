package Pekan5;

import java.util.ArrayList;

public class Rekening {
	private String nomorRekening;
	private String namaPemilik;
	private String pin;
	
	protected double saldo;
	protected ArrayList<Transaksi> riwayatTransaksi;
	
	public Rekening(String nomor, String nama, double saldoAwal, String pinAwal) {
		this.nomorRekening = nomor;
		this.namaPemilik = nama;
		this.saldo = saldoAwal;
		
		if (pinAwal.length() == 6) {
			this.pin = pinAwal;
		} else {
			System.out.println("Peringatan: PIN harus 6 digit! Menggunakan PIN default 123456");
			this.pin = "123456";
		}
		
		this.riwayatTransaksi = new ArrayList<>();
		System.out.println("Rekening atas nama " + namaPemilik + " berhasil dibuat dengan saldo Rp " + (saldo)); 
	}
	
	public String getNomorRekening() { return nomorRekening; }
	public String getNamaPemilik() { return namaPemilik; }
	
	public boolean otentikasi(String inputPin) {
		return this.pin.equals(inputPin);
	}
	
	public void setorTunai(double nominal) {
		if (nominal > 10000) {
			saldo += nominal;
			
			String idTrx = "TRX-S-" + System.currentTimeMillis();
			Transaksi trxBaru = new Transaksi(idTrx, "Kredit", nominal);
			riwayatTransaksi.add(trxBaru);
			
			System.out.println("Setor tunai Rp " + (nominal) + " berhasil. Saldo saat ini: Rp " + (saldo));
		} else {
			System.out.println("Gagal: Nominal setor harus lebih dari 10.000!");
		}
	}
	
	public boolean tarikTunai(double nominal) {
		if (nominal <= 0) {
			System.out.println("Gagal : Nominal tarik harus lebih dari 0!");
			return false;
		} else if (nominal > saldo) {
			System.out.println("Transaksi Gagal: Saldo tidak mencukupi. Saldo Anda: Rp " + (saldo));
			return false;
		} else {
			saldo -= nominal;
			
			String idTrx = "TRX-T-" + System.currentTimeMillis();
			riwayatTransaksi.add(new Transaksi(idTrx, "Debit", nominal));
			
			System.out.println("Tarik tunai Rp " + (nominal) + " berhasil. Sisa saldo: Rp " + (saldo));
			return true;
		}
	}
	
	public void cekInformasi() {
		System.out.println("--- INFO REKENING ---");
		System.out.println("No. Rekening : " + nomorRekening);
		System.out.println("Nama Pemilik : " + namaPemilik);
		System.out.println("Saldo Akhir  : Rp " + (saldo));
		System.out.println("-----------------------");
	}
	
	public void cetakMutasi() {
		if (riwayatTransaksi.isEmpty()) {
			System.out.println("Belum ada transaksi pada rekening ini");
		} else {
			System.out.println("--- Riwayat Transaksi ---");
			for (Transaksi trx : riwayatTransaksi) {
				trx.cetakDetail();
			}
			System.out.println("------------------------");
		}
	}
	
	public void cetakTigaTransaksiTerbaru() {
		if (riwayatTransaksi.size() <= 3) {
			System.out.println("Transaksi belum lebih dari 3!");
			return;
		}
		System.out.println("--- 3 Transaksi Terbaru ---");
		int jumlah = riwayatTransaksi.size();
		
		for (int i = jumlah -1; i >= jumlah - 3; i--) {
			riwayatTransaksi.get(i).cetakDetail();
		}
		System.out.println("-----------------------");
	}
	
	public void transfer(Rekening tujuan, double nominal, String pinInput) {
	    if (!otentikasi(pinInput)) {
	        System.out.println("Transfer gagal: PIN salah!");
	        return;
	    }

	    if (nominal <= 10000) {
	        System.out.println("Transfer gagal: Nominal harus lebih dari Rp10.000!");
	        return;
	    }

	    if (tujuan == this) {
	        System.out.println("Transfer gagal: Rekening tujuan tidak boleh sama dengan rekening pengirim!");
	        return;
	    }

	    if (this.tarikTunai(nominal)) {
	        tujuan.setorTunai(nominal);
	        System.out.println("Transfer berhasil!");
	    }
	}
}
