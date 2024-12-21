package com.NomDev.DePauseProject.entity;
// ref https://stackoverflow.com/questions/6667243/using-enum-values-as-string-literals
public enum TherapyType {
    INDIVIDUAL {
        @Override
        public String toString() {
            return "Individual Therapy";
        }
    },
    FAMILY {
        @Override
        public String toString() {
            return "Family Therapy";
        }
    },
    COUPLE {
        @Override
        public String toString() {
            return "Couple Therapy";
        }
    },
    CHILD {
        @Override
        public String toString() {
            return "Child Therapy";
        }
    };
}

