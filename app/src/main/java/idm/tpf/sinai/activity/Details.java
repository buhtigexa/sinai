package idm.tpf.sinai.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

import idm.tpf.sinai.R;
import idm.tpf.sinai.async.QueryHandler;
import idm.tpf.sinai.providers.Jobs;
import idm.tpf.sinai.providers.JobsProvider;


public class Details extends AbsDetail {

    protected QueryHandler queryHandler;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_delete:

                // acá tendría que abstraer un poco, y también puedo encapsular la operación que se haga según un SI/NO

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Sinai - Eliminar foto.");
                alertDialog.setMessage("¿ Estás seguro de borrarla ?");
                alertDialog.setIcon(R.drawable.ic_delete_forever_white_36dp);

                alertDialog.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        queryHandler = new QueryHandler(MainActivity.contentResolver);
                        queryHandler.startDelete(3, null, JobsProvider.CONTENT_URI, Jobs._ID + " = " + bundle.getLong(Jobs._ID), null);
                        Toast.makeText(getApplicationContext(), "¡ Eliminado !", Toast.LENGTH_LONG).show();
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();
                break;


            case R.id.action_share:
                send();
                break;
            case android.R.id.home:
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_photodetails, menu);
    }

    protected void send(){


        File imagePath= new File(bundle.getString(Jobs.PATH));
        Uri uri=Uri.parse(String.valueOf(Uri.fromFile(imagePath)));
        Intent i = new Intent(Intent.ACTION_SEND);

        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));


        Log.v(TAG," URI :" + uri.toString());
        Log.v(TAG, "URI Autoridad:" + uri.getEncodedAuthority());
        Log.v(TAG," URI HOST : " +  uri.getHost());
        Log.v(TAG, "URI PORT " + uri.getPort());
        Log.v(TAG, "URI SCHEME : " + uri.getScheme());
        Log.v(TAG, "URI  Last Path segment" + uri.getLastPathSegment());


        i.setType("image/jpg");
        i.putExtra(Intent.EXTRA_STREAM, uri);
        i.putExtra(Intent.EXTRA_SUBJECT, "Una foto de Sinai !");
        i.putExtra(Intent.EXTRA_TEXT, "Hey, te envío una foto que saqué usando Sinai ;) ");
        startActivity(Intent.createChooser(i, "Elegí una app para compartir la foto "));


    }

}
