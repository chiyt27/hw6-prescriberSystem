package chiyt;

import java.util.List;

public class Prescription {
	private String name;
	private String potentialDisease;
	private List<String> medicines;
	private String usage;

	public Prescription() {}

	public Prescription(String name, String potentialDisease, List<String> medicines, String usage) {
		this.name = name;
		this.potentialDisease = potentialDisease;
		this.medicines = medicines;
		this.usage = usage;
	}

	// Getters and Setters
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getPotentialDisease() { return potentialDisease; }
	public void setPotentialDisease(String potentialDisease) { this.potentialDisease = potentialDisease; }

	public List<String> getMedicines() { return medicines; }
	public void setMedicines(List<String> medicines) { this.medicines = medicines; }

	public String getUsage() { return usage; }
	public void setUsage(String usage) { this.usage = usage; }

	@Override
	public String toString() {
		return "Prescription{" +
				"name='" + name + '\'' +
				", potentialDisease='" + potentialDisease + '\'' +
				", medicines=" + medicines +
				", usage='" + usage + '\'' +
				'}';
	}
}
