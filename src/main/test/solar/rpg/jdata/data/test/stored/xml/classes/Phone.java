package solar.rpg.jdata.data.test.stored.xml.classes;

import org.jetbrains.annotations.Nullable;
import solar.rpg.jdata.data.stored.file.attribute.JAttributedField;
import solar.rpg.jdata.data.stored.file.JFileElement;
import solar.rpg.jdata.data.stored.file.attribute.JHasAttributes;

@JHasAttributes(names = "phoneType", types = Phone.PhoneType.class)
public class Phone extends JFileElement {

    /**
     * Country code of the phone number. The '+' prefix should not be included.
     */
    private JAttributedField<String> countryCode;
    private JAttributedField<String> phoneNumber;

    @Nullable
    public final String getCountryCode() {
        return countryCode.get();
    }

    @Nullable
    public final String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setCountryCode(@Nullable String countryCode) {
        this.countryCode.set(countryCode);
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public enum PhoneType {
        MOBILE,
        LANDLINE,
        WORK,
        OTHER
    }
}
