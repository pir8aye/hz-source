package nxt.http;

import nxt.Asset;
import nxt.Order;
import nxt.util.Convert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

import static nxt.http.JSONResponses.INCORRECT_ASSET;
import static nxt.http.JSONResponses.MISSING_ASSET;
import static nxt.http.JSONResponses.UNKNOWN_ASSET;

public final class GetBidOrderIds extends HttpRequestHandler {

    static final GetBidOrderIds instance = new GetBidOrderIds();

    private GetBidOrderIds() {}

    @Override
    JSONStreamAware processRequest(HttpServletRequest req) {

        String asset = req.getParameter("asset");
        if (asset == null) {
            return MISSING_ASSET;
        }

        long assetId;
        try {
            assetId = Convert.parseUnsignedLong(asset);
        } catch (RuntimeException e) {
            return INCORRECT_ASSET;
        }

        if (Asset.getAsset(assetId) == null) {
            return UNKNOWN_ASSET;
        }

        JSONArray orderIds = new JSONArray();
        Iterator<Order.Bid> bidOrders = Order.Bid.getSortedOrders(assetId).iterator();
        while (bidOrders.hasNext()) {
            orderIds.add(Convert.convert(bidOrders.next().getId()));
        }

        JSONObject response = new JSONObject();
        response.put("bidOrderIds", orderIds);
        return response;
    }

}
