package Pekan3;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Rekening {
	// 1. Mengunci atribut dengan 'private'
	private String nomorRekening;
	private String namaPemilik;
	private double saldo;
	private String pin; // Data sensitif
	
	// Implementasi Asosiasi (1-to-many)
	ArrayList<Transaksi> riwayatTransaksi;
	
	// 2. Modifikasi Constructor untuk menerima PIN awal
	public Rekening(String nomor, String nama, double saldoAwal, String pinAwal) {
		this.nomorRekening = nomor;
		this.namaPemilik = nama;
		this.saldo = saldoAwal;
		
		// Validasi PIN di dalam Constructor
		if (pinAwal.length() == 6) {
			this.pin = pinAwal;
		} else {
			System.out.println("Peringatan: PIN harus 6 digit! Menggunakan PIN default 123456");
			this.pin = "123456";
		}
		
		this.riwayatTransaksi = new ArrayList<>();
		System.out.println("Rekening atas nama " + namaPemilik + " berhasil dibuat dengan saldo Rp " + formatRupiah(saldo)); 
	}
	
	// 3. Getter untuk atribut yang diizinkan dibaca publik
	public String getNomorRekening() { return nomorRekening; }
	public String getNamaPemilik() { return namaPemilik; }
	
	// 4. Method Othentifikasi Internal (Validasi Enkapsulasi)
	public boolean otentifikasi(String inputPin) {
		return this.pin.equals(inputPin);
	}
	
	private String formatRupiah(double nominal) {
		DecimalFormatSymbols simbol = new DecimalFormatSymbols(new Locale("id", "ID"));
		simbol.setGroupingSeparator('.');
		simbol.setDecimalSeparator(',');
		
		DecimalFormat formatter = new DecimalFormat("#,##0.00", simbol);
		return formatter.format(nominal);
	}
	
	public void setorTunai(double nominal) {
		if (nominal > 10000) {
			saldo += nominal;
			// Merekam riwayat (Pembuatan Objek Transaksi di dalam memori
			String idTrx = "TRX-S-" + System.currentTimeMillis();
			Transaksi trxBaru = new Transaksi(idTrx, "Kredit", nominal);
			riwayatTransaksi.add(trxBaru);
			
			System.out.println("Setor tunai Rp " + formatRupiah(nominal) + " berhasil. Saldo saat ini: Rp " + formatRupiah(saldo));
		} else {
			System.out.println("Gagal: Nominal setor harus lebih dari 10.000!");
		}
	}
	
	public void tarikTunai(double nominal) {
		if (nominal < 10000) {
			System.out.println("Transaksi Gagal : Minimal nominal penarikan 10.000");
		} else if (nominal > saldo) {
			System.out.println("Transaksi Gagal: Saldo tidak mencukupi. Saldo Anda: Rp " + formatRupiah(saldo));
		} else {
			saldo -= nominal;
			String idTrx = "TRX-T-" + System.currentTimeMillis();
			Transaksi trxBaru = new Transaksi(idTrx, "Debit", nominal);
			riwayatTransaksi.add(trxBaru);
			
			System.out.println("Transaksi sebesar Rp " + formatRupiah(nominal) + " berhasil! Saldo anda tersisa: Rp " + formatRupiah(saldo));
		}
	}
	
	public void cekInformasi() {
		System.out.println("--- INFO REKENING ---");
		System.out.println("No. Rekening : " + nomorRekening);
		System.out.println("Nama Pemilik : " + namaPemilik);
		System.out.println("Saldo Akhir  : Rp " + formatRupiah(saldo));
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
}
