package com.jn.olio_ohjelmointiharkkatyo;

public class PopulationData {
        private int population;
        private int population_difference;
        private float employment;
        private float workplace_self_sufficiency;
        public PopulationData(int population, int population_difference, float employment, float workplace_self_sufficiency) {
            this.population = population;
            this.population_difference = population_difference;
            this.employment = employment;
            this.workplace_self_sufficiency = workplace_self_sufficiency;
        }

        public int getPopulation() {
            return population;
        }
        public int getPopulationDifference() {
            return population_difference;
        }

        public float getEmployment() {
            return employment;
        }

        public float getWorkplaceSelfSufficiency() {
            return workplace_self_sufficiency;
        }
}
