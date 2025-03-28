package com.example.productmanagement.activities.customer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.productmanagement.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomerAddActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText lastNameEditText, firstNameEditText, phoneEditText, emailEditText, birthdayEditText;
    private EditText addressEditText, postalCodeEditText, districtAutoComplete, wardAutoComplete;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton, otherRadioButton;
    private SwitchCompat marketingSwitch;
    private Calendar calendar;
    private boolean editMode = false;
    private String customerId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_add);

        // Khởi tạo Calendar
        calendar = Calendar.getInstance();

        // Khởi tạo các thành phần giao diện
        backButton = findViewById(R.id.back_button);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        birthdayEditText = findViewById(R.id.birthday_edit_text);
        genderRadioGroup = findViewById(R.id.gender_radio_group);
        maleRadioButton = findViewById(R.id.male_radio_button);
        femaleRadioButton = findViewById(R.id.female_radio_button);
        otherRadioButton = findViewById(R.id.other_radio_button);
        marketingSwitch = findViewById(R.id.marketing_switch);
        addressEditText = findViewById(R.id.address_edit_text);
        postalCodeEditText = findViewById(R.id.postal_code_edit_text);
        districtAutoComplete = findViewById(R.id.district_auto_complete);
        wardAutoComplete = findViewById(R.id.ward_auto_complete);

        // Tìm nút lưu
        View saveButton = findViewById(R.id.save_button);

        // Kiểm tra xem đang ở chế độ chỉnh sửa hay không
        if (getIntent().hasExtra("edit_mode")) {
            editMode = getIntent().getBooleanExtra("edit_mode", false);

            if (editMode && getIntent().hasExtra("customer_name") && getIntent().hasExtra("customer_phone")) {
                // Lấy thông tin khách hàng từ Intent
                String fullName = getIntent().getStringExtra("customer_name");
                String phone = getIntent().getStringExtra("customer_phone");
                String email = getIntent().getStringExtra("customer_email");
                String gender = getIntent().getStringExtra("customer_gender");
                String birthday = getIntent().getStringExtra("customer_birthday");
                String address = getIntent().getStringExtra("customer_address");

                // Tách họ và tên
                String[] nameParts = fullName.split(" ", 2);
                if (nameParts.length > 1) {
                    lastNameEditText.setText(nameParts[0]);
                    firstNameEditText.setText(nameParts[1]);
                } else {
                    firstNameEditText.setText(fullName);
                }

                phoneEditText.setText(phone);
                emailEditText.setText(email);
                birthdayEditText.setText(birthday);
                addressEditText.setText(address);

                // Thiết lập giới tính
                if ("Nam".equals(gender)) {
                    maleRadioButton.setChecked(true);
                } else if ("Nữ".equals(gender)) {
                    femaleRadioButton.setChecked(true);
                } else {
                    otherRadioButton.setChecked(true);
                }
            }
        }

        // Thiết lập sự kiện click cho nút quay lại
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Thiết lập sự kiện click cho trường ngày sinh
        birthdayEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Thiết lập dữ liệu mẫu cho các trường địa chỉ
        setupAddressFields();

        // Thiết lập sự kiện click cho nút lưu
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomer();
            }
        });
    }

    private void setupAddressFields() {
        // Thiết lập dữ liệu mẫu cho trường khu vực
        districtAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDistrictOptions();
            }
        });

        // Thiết lập dữ liệu mẫu cho trường phường/xã
        wardAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWardOptions();
            }
        });
    }

    private void showDistrictOptions() {
        final String[] districts = {"Quận Cẩm Lệ", "Quận Hải Châu", "Quận Liên Chiểu", "Quận Ngũ Hành Sơn", "Quận Sơn Trà", "Quận Thanh Khê"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Chọn khu vực");
        builder.setItems(districts, new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                districtAutoComplete.setText(districts[which]);
            }
        });
        builder.show();
    }

    private void showWardOptions() {
        final String[] wards = {"Phường Hòa An", "Phường Hòa Phát", "Phường Hòa Thọ Đông", "Phường Hòa Thọ Tây", "Phường Hòa Xuân", "Phường Khuê Trung"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Chọn phường/xã");
        builder.setItems(wards, new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                wardAutoComplete.setText(wards[which]);
            }
        });
        builder.show();
    }

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                birthdayEditText.setText(dateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(
                CustomerAddActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void saveCustomer() {
        String lastName = lastNameEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String birthday = birthdayEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String postalCode = postalCodeEditText.getText().toString().trim();
        String district = districtAutoComplete.getText().toString().trim();
        String ward = wardAutoComplete.getText().toString().trim();

        // Kiểm tra dữ liệu nhập vào
        if (lastName.isEmpty() || firstName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy giới tính được chọn
        String gender = "Khác";
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.male_radio_button) {
            gender = "Nam";
        } else if (selectedId == R.id.female_radio_button) {
            gender = "Nữ";
        }

        // Lấy trạng thái tiếp thị
        boolean wantsMarketing = marketingSwitch.isChecked();

        // Tạo địa chỉ đầy đủ
        String fullAddress = address;
        if (!ward.isEmpty()) {
            fullAddress += ", " + ward;
        }
        if (!district.isEmpty()) {
            fullAddress += ", " + district;
        }
        if (!postalCode.isEmpty()) {
            fullAddress += " (" + postalCode + ")";
        }

        // Trong thực tế, bạn sẽ lưu khách hàng vào cơ sở dữ liệu ở đây
        // Đối với ứng dụng mẫu này, chúng ta chỉ hiển thị thông báo thành công
        String message = editMode ? "Đã cập nhật khách hàng thành công" : "Đã thêm khách hàng thành công";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }
}

