package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static Schema schema;

    public static void main(String args[]) throws Exception {
        schema = new Schema(3, "greendao");

        Entity user = schema.addEntity("User");
        user.addLongProperty("id").primaryKey();
        user.addIntProperty("role_id");
        user.addStringProperty("avatar");
        user.addStringProperty("name");
        user.addStringProperty("fullname");
        user.addStringProperty("phone");
        user.addStringProperty("email");
        user.addStringProperty("created_at");
        user.addStringProperty("updated_at");
        user.addStringProperty("last_login");
        user.addIntProperty("is_active");
        user.addStringProperty("remember_token");

        Entity Case = schema.addEntity("CaseObj");
        Case.addLongProperty("id").primaryKey();
        Case.addLongProperty("dormitory_id");
        Case.addLongProperty("block_id");
        Case.addLongProperty("category_id");
        Case.addLongProperty("case_status_id");
        Case.addLongProperty("create_by");
        Case.addLongProperty("assign_by");
        Case.addLongProperty("assign_to");
        Case.addStringProperty("content");
        Case.addStringProperty("created_at");
        Case.addStringProperty("updated_at");
        Case.addStringProperty("spf_incident");
        Case.addStringProperty("case_investigation_name");
        Case.addStringProperty("case_investigation_contact");
        Case.addStringProperty("operative_remarks");
        Case.addStringProperty("government_agencies");
        Case.addStringProperty("head_ops_assessment");
        Case.addStringProperty("follow_up_timeline");
        Case.addStringProperty("assign_to_name");
        Case.addStringProperty("create_by_name");

        Entity dormitory = schema.addEntity("Dormitory");
        dormitory.addLongProperty("id").primaryKey();
        dormitory.addStringProperty("name");

        Entity block = schema.addEntity("Block");
        block.addLongProperty("id").primaryKey();
        block.addIntProperty("dormitory_id");
        block.addStringProperty("name");

        Entity level = schema.addEntity("Level");
        level.addLongProperty("id").primaryKey();
        level.addIntProperty("block_id");
        level.addStringProperty("name");

        Entity room = schema.addEntity("Room");
        room.addLongProperty("id").primaryKey();
        room.addIntProperty("level_id");
        room.addStringProperty("name");

        Entity category = schema.addEntity("Category");
        category.addLongProperty("id").primaryKey();
        category.addStringProperty("name");
        category.addStringProperty("description");

        Entity dormitory_category = schema.addEntity("DormitoryCategory");
        dormitory_category.addLongProperty("id").primaryKey();
        dormitory_category.addLongProperty("dormitory_id");
        dormitory_category.addLongProperty("category_id");

        Entity sub_category = schema.addEntity("SubCategory");
        sub_category.addLongProperty("id").primaryKey();
        sub_category.addLongProperty("category_id");
        sub_category.addStringProperty("name");
        sub_category.addStringProperty("description");

        Entity status = schema.addEntity("Status");
        status.addLongProperty("id").primaryKey();
        status.addStringProperty("name");

        Entity CaseImage = schema.addEntity("CaseImage");
        CaseImage.addLongProperty("id").primaryKey();
        CaseImage.addStringProperty("path");
        CaseImage.addLongProperty("case_id");

        Entity CaseSubCategory = schema.addEntity("CaseSubCategory");
        CaseSubCategory.addLongProperty("id").primaryKey();
        CaseSubCategory.addStringProperty("name");
        CaseSubCategory.addStringProperty("description");
        CaseSubCategory.addLongProperty("case_id");

        Entity CaseActionTaken = schema.addEntity("CaseActionTaken");
        CaseActionTaken.addLongProperty("id").primaryKey();
        CaseActionTaken.addStringProperty("name");
        CaseActionTaken.addIntProperty("order");
        CaseActionTaken.addLongProperty("case_id");

        Entity CaseReferredTo = schema.addEntity("CaseReferredTo");
        CaseReferredTo.addLongProperty("id").primaryKey();
        CaseReferredTo.addStringProperty("name");
        CaseReferredTo.addIntProperty("order");
        CaseReferredTo.addLongProperty("case_id");

        Entity staff = schema.addEntity("Staff");
        staff.addLongProperty("id").primaryKey();
        staff.addStringProperty("name");

        Entity worker = schema.addEntity("Worker");
        worker.addLongProperty("id").primaryKey();
        worker.addStringProperty("barcode");
        worker.addStringProperty("name");
        worker.addStringProperty("nationality_code");
        worker.addStringProperty("passport");
        worker.addStringProperty("expiry");
        worker.addIntProperty("sex");
        worker.addStringProperty("company");
        worker.addStringProperty("work_permit");
        worker.addLongProperty("dormitory_id");
        worker.addLongProperty("block_id");
        worker.addLongProperty("level_id");
        worker.addLongProperty("room_id");
        worker.addStringProperty("unit_number");
        worker.addStringProperty("image_1");
        worker.addStringProperty("image_2");
        worker.addStringProperty("image_3");
        worker.addStringProperty("created_at");
        worker.addStringProperty("updated_at");
        worker.addStringProperty("log_message");

        Entity nationality = schema.addEntity("Nationality");
        nationality.addStringProperty("code").primaryKey();
        nationality.addStringProperty("slug");
        nationality.addStringProperty("name");
        nationality.addIntProperty("order");

        Entity diary_type = schema.addEntity("DiaryType");
        diary_type.addLongProperty("id").primaryKey();
        diary_type.addStringProperty("name");

        Entity diary_all = schema.addEntity("DiaryAll");
        diary_all.addLongProperty("id").primaryKey();
        diary_all.addLongProperty("diary_type_id");
        diary_all.addStringProperty("content");
        diary_all.addStringProperty("created_at");
        diary_all.addStringProperty("created_by_name");

        Entity diary_visitor = schema.addEntity("DiaryVisitor");
        diary_visitor.addLongProperty("id").primaryKey();
        diary_visitor.addLongProperty("diary_type_id");
        diary_visitor.addIntProperty("is_staff");
        diary_visitor.addLongProperty("user_id");
        diary_visitor.addStringProperty("name");
        diary_visitor.addStringProperty("content");
        diary_visitor.addStringProperty("image_1");
        diary_visitor.addStringProperty("image_2");
        diary_visitor.addStringProperty("enter_time");
        diary_visitor.addStringProperty("exit_time");
        diary_visitor.addIntProperty("is_exited");
        diary_visitor.addLongProperty("created_by");

        Entity diary_shift = schema.addEntity("DiaryShift");
        diary_shift.addLongProperty("id").primaryKey();
        diary_shift.addLongProperty("diary_type_id");
        diary_shift.addLongProperty("user_id");
        diary_shift.addStringProperty("name");
        diary_shift.addStringProperty("content");
        diary_shift.addStringProperty("enter_time");
        diary_shift.addStringProperty("exit_time");
        diary_shift.addIntProperty("is_exited");
        diary_shift.addLongProperty("created_by");

        Entity diary_other = schema.addEntity("DiaryOther");
        diary_other.addLongProperty("id").primaryKey();
        diary_other.addLongProperty("diary_type_id");
        diary_other.addStringProperty("content");
        diary_other.addLongProperty("created_by");
        diary_other.addStringProperty("created_at");
        diary_other.addLongProperty("status");
        diary_other.addStringProperty("created_by_name");

        new DaoGenerator().generateAll(schema, args[0]);
    }

}
