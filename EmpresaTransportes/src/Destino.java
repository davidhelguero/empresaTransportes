
public class Destino {
	
	private String destino;
	private int km;
	
	Destino(String destino,int km){
		setDestino(destino);
		setKm(km);
	}

	private void setDestino(String destino) {
		if(!destino.isBlank())
			this.destino = destino.toLowerCase();
		else
			throw new RuntimeException("El destino no puede estar vacío");
	}
	
	private void setKm(int km) {
		if(km > 0)
			this.km = km;
		else
			throw new RuntimeException("El km debe ser mayor a 0");
	}

	public String getDestino() {
		return destino;
	}

	public int getKm() {
		return km;
	}
	
	public String toString() {
		StringBuilder cad = new StringBuilder("Destino: ");
		cad.append(destino);
		cad.append("\nDistancia: ");
		cad.append(km);
		cad.append("km");
		return cad.toString();
	}
	
}
