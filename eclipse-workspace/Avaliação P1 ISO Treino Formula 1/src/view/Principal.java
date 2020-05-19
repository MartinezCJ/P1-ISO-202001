package view;

import controller.ThreadCorrida;

import java.util.concurrent.Semaphore;

public class Principal {
	
	public static void main(String[] args) {

		int idCarro = 1;
		int permissoesPista = 5;
		Semaphore entradaPista = new Semaphore(permissoesPista);

		for (int idEscuderia = 1; idEscuderia <= 7; idEscuderia++) {
			Thread corrida = new ThreadCorrida (idCarro, idEscuderia, entradaPista);
			corrida.start();
		}
	}
}
