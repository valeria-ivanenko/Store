package com.example.ais_store.Processor;

import com.example.ais_store.Commands.Commands;
import com.example.ais_store.DB.DbUtils;
import com.example.ais_store.Message.MessageInfo;
import com.example.ais_store.Models.Category;
import com.example.ais_store.Models.Product;
import com.example.ais_store.Packet.Packet;
import com.example.ais_store.Packet.PacketInfo;

public class Processor {
    public static PacketInfo process(PacketInfo packetInfo) throws Exception {
        StringBuilder response = new StringBuilder();

        Commands command = Commands.values()[packetInfo.bMsg.cType];

        switch (command) {
            case ADD_PRODUCT:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split("\\$");
                if (parameters.length != 4 || DbUtils.showCategoryByID(Integer.parseInt(parameters[1])) == null) {
                    response.append("0");
                } else {
                    DbUtils.insertProduct(
                            Integer.parseInt(parameters[1]),
                            parameters[0],
                            Double.parseDouble(parameters[2]),
                            Integer.parseInt(parameters[3]));

                    response.append("\tProduct was added");
                }
                break;
            }
            case ADD_CATEGORY:
            {
                String parameter = new String(packetInfo.bMsg.message);

                DbUtils.insertCategory(parameter);

                response.append("\tCategory was added");
                break;
            }
            case DELETE_PRODUCT_BY_ID:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split(" ");
                if (parameters.length != 1) {
                    throw new Exception("Incorrect parameters");
                }

                int id = Integer.parseInt(parameters[0]);
                Product product = DbUtils.showProductByID(id);
                if (product != null) {
                    DbUtils.deleteProduct(id);
                    response.append("\tProduct was deleted");
                } else {
                    throw new Exception("There is no product with such id");
                }
                break;
            }
            case DELETE_CATEGORY_BY_ID:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split(" ");
                if (parameters.length != 1) {
                    throw new Exception("Incorrect parameters");
                }

                int id = Integer.parseInt(parameters[0]);
                Category category = DbUtils.showCategoryByID(id);
                if (category != null) {
                    DbUtils.deleteCategory(id);
                    response.append("\tCategory was deleted");
                } else {
                    throw new Exception("There is no category with such id");
                }
                break;
            }
            case UPDATE_PRODUCT_ID:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split(" ");
                if (parameters.length != 2) {
                    throw new Exception("Incorrect parameters");
                }

                DbUtils.updateIdProduct(
                        Integer.parseInt(parameters[0]),
                        Integer.parseInt(parameters[1]));

                response.append("\tId of product was changed");
                break;
            }
            case UPDATE_PRODUCT_NAME:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split("\\$");
                if (parameters.length != 2) {
                    throw new Exception("Incorrect parameters");
                }

                DbUtils.updateProductName(parameters[1], Integer.parseInt(parameters[0]));

                response.append("\tName of product was changed");
                break;
            }
            case UPDATE_PRODUCT_CATEGORY:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split(" ");
                if (parameters.length != 2 || DbUtils.showCategoryByID(Integer.parseInt(parameters[1])) == null) {
                    response.append("0");
                } else {
                    int id = Integer.parseInt(parameters[0]);
                    int categoryId = Integer.parseInt(parameters[1]);

                    DbUtils.updateIdCategoryForProduct(
                            categoryId,
                            id);

                    response.append("\tCategory of product was changed");
                }

                break;
            }
            case UPDATE_PRODUCT_PRICE:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split(" ");
                if (parameters.length != 2) {
                    throw new Exception("Incorrect parameters");
                }

                int id = Integer.parseInt(parameters[0]);
                double price = Double.parseDouble(parameters[1]);

                DbUtils.updatePrice( price, id);

                response.append("\tPrice of product was changed");
                break;
            }
            case UPDATE_PRODUCT_NUMBER:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split(" ");
                if (parameters.length != 2) {
                    throw new Exception("Incorrect parameters");
                }

                int id = Integer.parseInt(parameters[0]);
                int product_number = Integer.parseInt(parameters[1]);

                DbUtils.updateProductNumber(product_number, id);

                response.append("\tProduct number of product was changed");
                break;
            }
            case UPDATE_CATEGORY_ID:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split(" ");
                if (parameters.length != 2) {
                    throw new Exception("Incorrect parameters");
                }

                DbUtils.updateIdCategory(
                        Integer.parseInt(parameters[1]),
                        Integer.parseInt(parameters[0]));

                response.append("\tId of category was changed");
                break;
            }
            case UPDATE_CATEGORY_NAME:
            {
                String[] parameters = new String(packetInfo.bMsg.message).split("\\$");
                if (parameters.length != 2) {
                    throw new Exception("Incorrect parameters");
                }

                DbUtils.updateCategoryName(
                        parameters[1],
                        Integer.parseInt(parameters[0]));

                response.append("\tName of category was changed");
                break;
            }
            default:
                response = new StringBuilder("No such command");
                break;

        }

        return new PacketInfo(
                packetInfo.bSrc,
                Packet.getFreePacketId(),
                packetInfo.wLen,
                new MessageInfo(
                        command.ordinal(),
                        packetInfo.bMsg.bUserId,
                        response.toString().getBytes()));
    }
}
