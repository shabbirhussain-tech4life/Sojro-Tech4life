package com.mdcbeta.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Shakil Karim on 2/18/17.
 */

public class Farmer  {

        @SerializedName("village")
        @Expose
        public String village;
        @SerializedName("enterpreneur")
        @Expose
        public String enterpreneur;
        @SerializedName("unioncouncil")
        @Expose
        public String unioncouncil;
        @SerializedName("district")
        @Expose
        public String district;
        @SerializedName("date")
        @Expose
        public String date;

        @SerializedName("id")
        @Expose
        public int id;

        @SerializedName("beneficiary")
        @Expose
        public String beneficiary;
        @SerializedName("fathername_husbandname")
        @Expose
        public String fathernameHusbandname;

        @SerializedName("cnic")
        @Expose
        public String cnic;

        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("education")
        @Expose
        public String education;
        @SerializedName("marital_status")
        @Expose
        public String maritalStatus;
        @SerializedName("milking_animal_cow")
        @Expose
        public int milkingAnimalCow;
        @SerializedName("milking_animal_buf")
        @Expose
        public int milkingAnimalBuf;
        @SerializedName("dry_animal_cow")
        @Expose
        public int dryAnimalCow;
        @SerializedName("dry_animal_buf")
        @Expose
        public int dryAnimalBuf;
        @SerializedName("hiefers_calves")
        @Expose
        public String hiefersCalves;
        @SerializedName("small_animal_goat")
        @Expose
        public int smallAnimalGoat;
        @SerializedName("small_animal_sheep")
        @Expose
        public int smallAnimalSheep;
        @SerializedName("small_animal_poultry")
        @Expose
        public int smallAnimalPoultry;
        @SerializedName("mortality_large")
        @Expose
        public int mortalityLarge;
        @SerializedName("mortality_small")
        @Expose
        public int mortalitySmall;
        @SerializedName("total_milk_prod_LPD")
        @Expose
        public String totalMilkProdLPD;
        @SerializedName("price_of_milk_per_liter")
        @Expose
        public int priceOfMilkPerLiter;
        @SerializedName("marketing_channel_dhoodhi")
        @Expose
        public String marketingChannelDhoodhi;
        @SerializedName("marketing_channel_processor")
        @Expose
        public String marketingChannelProcessor;
        @SerializedName("marketing_channel_hotelname_home")
        @Expose
        public String marketingChannelHotelnameHome;
        @SerializedName("signature")
        @Expose
        public String signature;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

        @SerializedName("created_at")
        @Expose
        public long createdAt;

}

