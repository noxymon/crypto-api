package id.noxymon.miner.crawler.plugins.trader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AssetInfoReturnModel {
    @JsonProperty("server_time")
    private Integer serverTime;
    private AssetInfoBalanceModel balance;
    @JsonProperty("balance_hold")
    private AssetInfoBalanceReturnModel balanceHold;
    @JsonProperty("user_id")
    private String userId;
    private String name;
    private String email;
    @JsonProperty("profile_picture")
    private Object profilePicture;
    @JsonProperty("verification_status")
    private String verificationStatus;
    @JsonProperty("gauth_enable")
    private Boolean gauthEnable;
}
