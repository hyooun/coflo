package com.reviewping.coflo.service.dto;

import static org.eclipse.jgit.diff.DiffEntry.*;

public record GitFileInfo(ChangeType changeType, String filePath) {}
