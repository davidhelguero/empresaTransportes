
public class MegaTrailer extends Transporte{
	private double segCarga;
	private double costoFijo;
	private double comida;
	
	MegaTrailer(String idTransp, double cargaMax, double capacidad, boolean frigorifico, double costoKm, double segCarga, double costoFijo, double comida){
		super(idTransp,cargaMax,capacidad,frigorifico,costoKm);
		setSegCarga(segCarga);
		setCostoFijo(costoFijo);
		setComida(comida);
	}
	
	//SETTERS
	
	private void setSegCarga(double segCarga) {
		if(segCarga>0)
			this.segCarga = segCarga;
		else
			throw new RuntimeException("El seguro de carga debe ser mayor a 0");
	}

	private void setCostoFijo(double costoFijo) {
		if(costoFijo>0)
			this.costoFijo = costoFijo;
		else
			throw new RuntimeException("El costo fijo debe ser mayor a 0");
	}

	private void setComida(double comida) {
		if(comida>0)
			this.comida = comida;
		else
			throw new RuntimeException("El costo de comida debe ser mayor a 0");
	}
	
	//MÉTODOS
	
	public void asignarDestino(String destino,int km) {
		if(km > 500)
			super.asignarDestino(destino, km);
		else
			throw new RuntimeException("Un megatráiler sólo puede hacer viajes a más de 500 kilómetros");
	}
	
	public double obtenerCostoViaje() {
		return super.obtenerCostoViaje() + segCarga + costoFijo + comida;
	}
	
	public boolean equals(Object o) {
		if(o instanceof MegaTrailer)
			return super.equals(o);
		return false;
	}
	
	public String toString() {
		StringBuilder cad = new StringBuilder("Tranporte: Megatráiler\n");
		cad.append(super.toString());
		cad.append("\nCosto fijo: $");
		cad.append(costoFijo);
		cad.append("\nCosto por comida: $");
		cad.append(comida);
		cad.append("\nSeguro de carga: ");
		cad.append(segCarga);
		return cad.toString();
	}
}
