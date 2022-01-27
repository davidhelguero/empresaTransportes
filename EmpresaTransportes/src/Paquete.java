
public class Paquete {
	
	private String destino;
	private double peso;
	private double volumen;
	private boolean frio;
	
	Paquete(String destino,double peso,double volumen,boolean frio){
		setDestino(destino);
		setPeso(peso);
		setVolumen(volumen);
		this.frio = frio;
	}
	
	private void setDestino(String destino) {
		if(!destino.isBlank())
			this.destino = destino.toLowerCase();
		else
			throw new RuntimeException("El destino no puede estar vacío");
	}

	private void setPeso(double peso) {
		if(peso > 0)
			this.peso = peso;
		else
			throw new RuntimeException("El peso debe ser mayor a 0");
	}

	private void setVolumen(double volumen) {
		if(volumen > 0)
			this.volumen = volumen;
		else
			throw new RuntimeException("El peso debe ser mayor a 0");
	}
	
	public double getPeso() {
		return peso;
	}
	
	public double getVolumen() {
		return volumen;
	}
	
	public String getDestino() {
		return destino;
	}
	
	public boolean isFrio() {
		return frio;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Paquete) {
				Paquete paquete = (Paquete)o;
				return destino.equals(paquete.getDestino()) &&
					   peso == paquete.getPeso() && 
					   volumen == paquete.getVolumen() &&
					   frio == paquete.isFrio();
		}
		return false;
	} 
	
}
