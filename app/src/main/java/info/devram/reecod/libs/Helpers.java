package info.devram.reecod.libs;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Currency;
import java.util.Locale;

public class Helpers {

    public static String formatDate(String date) {
        return LocalDate.parse(date)
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                        .withLocale(new Locale("en", "IN")));
    }

    public static boolean checkPermission(Context context) {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    public static void dismissKeyboard(FragmentActivity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String makeFirstCapital(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String formatCurrency(int amount) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        format.setCurrency(Currency.getInstance("INR"));
        return format.format(amount);
    }
}
