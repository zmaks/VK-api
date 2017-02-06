package tk.dzrcc;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Maksim on 26.01.2017.
 */
public class Commentator {
    private static final String TOKEN = "";
    private static final String TOKEN2 = "";
    private static final Integer MY_ID = 1;
    private static final Integer MY_ID2 = 1;
    private static final Integer TARGET_ID = 1;
    private static final String MASSAGE = "";
    private static final String MASSAGE2 = "";

    private TransportClient transportClient;
    private VkApiClient vk;
    private UserActor actor;
    private UserActor actor2;

    public Commentator(){
        transportClient = HttpTransportClient.getInstance();
        vk = new VkApiClient(transportClient);
        actor = new UserActor(MY_ID, TOKEN);
        actor2 = new UserActor(MY_ID2, TOKEN2);
    }

    public void startTimer(){
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = null;
        try {
            date = dateFormatter.parse("26-01-2017 20:32:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Timer timer = new Timer();
        timer.schedule(new CommentTask(),date);
    }

    private class CommentTask extends TimerTask
    {

        public void run()
        {
            System.out.println("RUN");
            GetResponse getResponse = null;
            Integer post_id = 0;
            boolean checked = false;

            try {
                while (true) {
                    getResponse = vk.wall().get(actor).ownerId(TARGET_ID).count(1).execute();

                    if (!getResponse.getItems().isEmpty()) {
                        if (!checked)
                            post_id = getResponse.getItems().get(0).getId();
                        checked = true;
                        if (!getResponse.getItems().get(0).getId().equals(post_id)) {
                            System.out.println("Founded. Add comment");

                            vk.wall().createComment(actor, getResponse.getItems().get(0).getId()).ownerId(TARGET_ID).message(MASSAGE).execute();
                            if (!TOKEN2.equals(""))
                                vk.wall().createComment(actor2, getResponse.getItems().get(0).getId()).ownerId(TARGET_ID).message(MASSAGE2).execute();
                            break;
                        }
                    }
                    Thread.sleep(300L);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }
}
