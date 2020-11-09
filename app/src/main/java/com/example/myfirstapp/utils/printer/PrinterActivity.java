package com.example.myfirstapp.utils.printer;
/*import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;

public class PrinterActivity extends ActivityBase implements PrinterToBoleta.IPrinterListener {

    public static final int REQUEST_CODE_PRINTER = 1000002;
    public static final String KEY_RESULT_MESSAGE = "KEY_RESULT_MESSAGE";

    public static final String KEY_NUM_TICKETS = "NUM_TICKETS";
    public static final String KEY_DUPLICADO = "DUPLICADO";

    public static final String TYPE_PRINT = "TYPE_PRINT";
    public static final int PRINT_BOLETA = 1;
    public static final int PRINT_COMPROBANTE = 2;
    public static final int PRINT_COMPROBANTE_AND_VOUCHER = 3;
    public static final int PRINT_COMPROBANTE_AND_DUPLICADO = 4;
    public static final int PRINT_DUPLICADO = 5;
    public static final int PRINT_BOLETA_AND_VOUCHER = 6;
    public static final int PRINT_BOLETA_AND_DUPLICADO = 7;
    public static final int PRINT_TICKET = 8;

    private static final String TAG_PRINT = "TAG_PRINT";

    private DialogGenericPrintBinding binding;
    private int density;
    private PrinterToBoleta printer;

    private TemporalDataDAO temporalDataDAO = TemporalDataDAO.getInstance();

    private long startActivityTime = 0;
    int typePrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.dialog_generic_print);
        density = (int) getResources().getDisplayMetrics().density;

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        startActivityTime = System.currentTimeMillis();

        String title = "Imprimiendo";
        typePrint = getIntent().getIntExtra(TYPE_PRINT, 0);
        switch (typePrint) {
            case PRINT_BOLETA:
                title += " boleta";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadPrinter();
                        try {
                            JsonNode boletaJson = temporalDataDAO.getBoletaTemporal();
                            temporalDataDAO.setBoletaTemporal(null);
                            int numTickets = getIntent().getIntExtra(KEY_NUM_TICKETS, 0);
                            printer.downloadImagesAndPrintBoletaJson(boletaJson, numTickets, dataSavedManager);
                        } catch (Exception e) {
                            endWithMessage("Problema con los datos de la impresion");
                        }
                    }
                }).start();
                break;
            case PRINT_COMPROBANTE:
                title += " comprobante";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadPrinter();
                        try {
                            Comprobante comprobante = dataSavedManager.getDataCart().getComprobante();

                            printer.printComprobante(comprobante);
                        } catch (Exception e) {
                            endWithMessage("Problema con los datos de la impresion");
                        }
                    }
                }).start();
                break;

            case PRINT_TICKET:
                title += " ticket";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadPrinter();
                        try {
                            JsonNode boletaJson = temporalDataDAO.getBoletaTemporal();
                            temporalDataDAO.setBoletaTemporal(null);

                            printer.printTicketCambio(boletaJson, 1);
                        } catch (Exception e) {
                            endWithMessage("Problema con los datos de la impresion");
                        }
                    }
                }).start();
                break;

            default:
                endCancel();
                break;
        }

        showPrintBoleta(title);

    }

    private void showPrintBoleta(String title) {
        binding.imageOk.setVisibility(View.GONE);

        binding.imgIcon.setImageDrawable(getDrawable(R.drawable.icon_boleta_print));

        binding.txtTitle.setText(title);

        binding.txtMsg.setText("");

        binding.layoutButtons.setVisibility(View.INVISIBLE);

        ObjectAnimator moveY = ObjectAnimator.ofFloat(binding.imgIcon, View.TRANSLATION_Y, 0f, -93f * density);

        AnimatorSet moveUp = new AnimatorSet();
        moveUp.play(moveY);
        moveUp.setDuration(3000);

        binding.imgIcon.setTag(moveUp);
        moveUp.start();

        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1000);
        animation1.setFillAfter(true);
        binding.getRoot().startAnimation(animation1);
        binding.txtTitle.setTag(TAG_PRINT);

        addDots(binding.txtTitle, title);
    }

    private void loadPrinter() {
        printer = SearchPrinter.searchPrinter(PrinterActivity.this, PrinterActivity.this);
        if (printer == null) {
            endWithMessage("Bluetooth is not available");
        }
    }

    private void endCancel() {
        Intent data = new Intent();
        data.putExtra("TYPE_PRINT", typePrint);
        setResult(RESULT_CANCELED, data);
        finish();
    }

    private void endWithMessage(String message) {
        Intent data = new Intent();
        data.putExtra("KEY_RESULT_MESSAGE", message);
        data.putExtra("TYPE_PRINT", typePrint);
        setResult(RESULT_CANCELED, data);
        finish();
    }

    private void endExito() {
        Intent data = new Intent();
        data.putExtra("TYPE_PRINT", typePrint);
        setResult(RESULT_OK, data);
        finish();
    }

    private void addDots(final TextView tv, final String title) {
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    int count = 0;
                    while (isActive) {
                        count++;
                        final int i = count;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView
                                StringBuffer dots = new StringBuffer("<font color=#70578B>" + title);
                                switch (i) {
                                    case 1:
                                        dots.append("</font><font color=#ffffff>...</font>");
                                        break;
                                    case 2:
                                        dots.append(".</font><font color=#ffffff>..</font>");
                                        break;
                                    case 3:
                                        dots.append("..</font><font color=#ffffff>.</font>");
                                        break;
                                    case 4:
                                        dots.append("...</font>");
                                        break;
                                }
                                tv.setText(Html.fromHtml(dots.toString()));
                            }
                        });
                        if (count == 4)
                            count = 0;
                        Thread.sleep(400);
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        thread.start();
    }

    @Override
    public void onRequestData(RequestDataType requestData) {

    }

    @Override
    public void onInvalidSession() {

    }

    @Override
    public void onErrorDialogReintent(RequestDataType requestData) {

    }

    @Override
    public void connectedPrinter() {

    }

    @Override
    public void endPrint() {
        endExito();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void warning(String warning) {
        endWithMessage(warning);
    }

    @Override
    public void error(String error) {
        endWithMessage(error);
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - startActivityTime > 30000) {
            endCancel();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}*/
