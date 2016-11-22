package com.halcyonmobile.jiraversionrelease;

import com.google.gson.annotations.Expose;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

class JiraVersion {

    @Expose
    private String id;
    @Expose
    private String description;
    @Expose
    private String name;
    @Expose
    private boolean released;
    @Expose
    private String releaseDate;
    @Expose
    private String project;

    JiraVersion(String name, @NotNull String project) {
        this.name = name;
        this.project = project;
    }

    JiraVersion release(@Nullable String description) {
        if (description != null && !description.isEmpty()) {
            this.description = description;
        }
        this.releaseDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.released = true;
        return this;
    }


    boolean isReleased() {
        return released;
    }

    String getName() {
        return name;
    }

    String getId() {
        return id;
    }
}
