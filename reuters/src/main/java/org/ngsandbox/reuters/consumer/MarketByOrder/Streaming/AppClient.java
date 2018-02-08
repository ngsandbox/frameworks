package org.ngsandbox.reuters.consumer.MarketByOrder.Streaming;

import com.thomsonreuters.ema.access.*;

class AppClient implements OmmConsumerClient {
    public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event) {
        System.out.println("Item Name: " + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
        System.out.println("Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));

        System.out.println("Item State: " + refreshMsg.state());

        if (DataType.DataTypes.MAP == refreshMsg.payload().dataType())
            decode(refreshMsg.payload().map());

        System.out.println();
    }

    public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event) {
        System.out.println("Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
        System.out.println("Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));

        if (DataType.DataTypes.MAP == updateMsg.payload().dataType())
            decode(updateMsg.payload().map());

        System.out.println();
    }

    public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event) {
        System.out.println("Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
        System.out.println("Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));

        if (statusMsg.hasState())
            System.out.println("Item State: " + statusMsg.state());

        System.out.println();
    }

    public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent) {
    }

    public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent) {
    }

    public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent) {
    }

    void decode(FieldList fieldList) {
        for (FieldEntry fieldEntry : fieldList) {
            System.out.println("Fid: " + fieldEntry.fieldId() + " Name: " + fieldEntry.name() + " value: " + fieldEntry.load());
        }
    }

    void decode(Map map) {
        if (DataType.DataTypes.FIELD_LIST == map.summaryData().dataType()) {
            System.out.println("Map Summary data:");
            decode(map.summaryData().fieldList());
            System.out.println();
        }

        for (MapEntry mapEntry : map) {
            if (DataType.DataTypes.BUFFER == mapEntry.key().dataType())
                System.out.println("Action: " + mapEntry.mapActionAsString() + " key value: " + EmaUtility.asHexString(mapEntry.key().buffer().buffer()));

            if (DataType.DataTypes.FIELD_LIST == mapEntry.loadType()) {
                System.out.println("Entry data:");
                decode(mapEntry.fieldList());
                System.out.println();
            }
        }
    }
}
