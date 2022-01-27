
public class Trailer extends Transporte{
	private double segCarga;
	
	Trailer(String idTransp, double cargaMax, double capacidad, boolean frigorifico, double costoKm, double segCarga){
		super(idTransp,cargaMax,capacidad,frigorifico,costoKm);
		setSegCarga(segCarga);
	}
	
	private void setSegCarga(double segCarga) {
		if(segCarga>0)
			this.segCarga = segCarga;
		else
			throw new RuntimeException("El seguro de carga debe ser mayor a 0");
	}
	
	//MÉTODOS
	
	public void asignarDestino(String destino,int km) {
		if(km>0 && km<=500)
			super.asignarDestino(destino, km);
		else	
			throw new RuntimeException("Un tráiler no puede hacer una distancia de más de 500 kilómetros");
	}
	
	public double obtenerCostoViaje() {
		return super.obtenerCostoViaje() + segCarga;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Trailer)
			return super.equals(o);
		return false;
	}
	
 	public String toString() {
		StringBuilder cad = new StringBuilder("Transporte: Tráiler\n");
		cad.append(super.toString());
		cad.append("\nSeguro de carga: ");
		cad.append(segCarga);
		return cad.toString();
	}
}
