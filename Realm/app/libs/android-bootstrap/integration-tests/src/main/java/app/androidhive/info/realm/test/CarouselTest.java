

package app.androidhive.info.realm.test;

import android.test.ActivityInstrumentationTestCase2;

import app.androidhive.info.realm.ui.CarouselActivity;


/**
 * Test displaying of carousel.
 */
public class CarouselTest extends ActivityInstrumentationTestCase2<CarouselActivity> {

    /**
     * Create test for {@link CarouselActivity}
     */
    public CarouselTest() {
        super(CarouselActivity.class);
    }

    /**
     * Verify activity exists
     */
    public void testActivityExists() {
        assertNotNull(getActivity());
    }
}
