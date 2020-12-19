package pharmacy.catalog;

import static org.salespointframework.core.Currencies.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import pharmacy.catalog.Medicine.IngredientType;
import pharmacy.catalog.Medicine.MedicineType;
import pharmacy.catalog.Medicine.PrescriptionType;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A {@link DataInitializer} implementation that will create dummy data for the application on application startup.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 * @see DataInitializer
 */
@Component
@Order(20)
class CatalogDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(CatalogDataInitializer.class);

	private final MedicineCatalog medicineCatalog;

	CatalogDataInitializer(MedicineCatalog medicineCatalog) {

		Assert.notNull(medicineCatalog, "MedicineCatalog must not be null!");

		this.medicineCatalog = medicineCatalog;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		if (medicineCatalog.findAll().iterator().hasNext()) {
			return;
		}

		LOG.info("Creating default catalog entries.");
		ArrayList<LocalDate> bbd = null;
		ArrayList<Medicine> ingredients = null;


		medicineCatalog.save(new Medicine("1", "Medikament 1", "med1", "usage1", 1, 10, Money.of(100, EURO), bbd, ingredients, PrescriptionType.PRESONLY, IngredientType.LABOR, MedicineType.LIQUID));
		medicineCatalog.save(new Medicine("2", "Medikament 2", "med2", "usage2", 2, 20, Money.of(200, EURO), bbd, ingredients, PrescriptionType.WITHOUTPRES, IngredientType.MIXTURE, MedicineType.CAPSULE));
		medicineCatalog.save(new Medicine("3", "Medikament 3", "med3", "usage3", 3, 30, Money.of(300, EURO), bbd, ingredients, PrescriptionType.PRESONLY, IngredientType.SHOP, MedicineType.POWDER));
		medicineCatalog.save(new Medicine("4", "Medikament 4", "med4", "usage4", 4, 40, Money.of(400, EURO), bbd, ingredients, PrescriptionType.WITHOUTPRES, IngredientType.BOTH, MedicineType.TABLET));

	}
}