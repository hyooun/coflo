import { ProjectRequest } from '@apis/Project';
import { ProjectRequestResponse } from 'types/language';
import { ProgrammingLanguagesData } from 'types/language';
import { ProgrammingLanguage } from 'types/language';
import { Language } from 'types/language';
import { ProjectDetailResponse } from 'types/language';

export const fetchProjectDetail = async (
  projectId: string,
): Promise<ProjectDetailResponse | null> => {
  const response = await ProjectRequest.getProjectDetail(projectId);
  if (response.data) {
    const projectData: ProjectRequestResponse = response.data;

    const programmingLanguagesData: ProgrammingLanguagesData = {
      labels: ['languages'],
      datasets: projectData.languages.map((lang: ProgrammingLanguage, index: number) => ({
        label: lang.name,
        data: [lang.share],
        backgroundColor: lang.color,
        barThickness: 10,
        borderSkipped: false,
        borderRadius:
          projectData.languages.length === 1
            ? [{ topLeft: 20, topRight: 20, bottomLeft: 20, bottomRight: 20 }]
            : index === 0
              ? [{ topLeft: 20, topRight: 0, bottomLeft: 20, bottomRight: 0 }]
              : index === projectData.languages.length - 1
                ? [{ topLeft: 0, topRight: 20, bottomLeft: 0, bottomRight: 20 }]
                : undefined,
      })),
    };

    const commitCount: number = projectData.commitCount;
    const branchCount: number = projectData.branchCount;
    const mergeRequestCount: number = projectData.mergeRequestCount;
    const aiReviewCount: number = projectData.aiReviewCount;

    console.log('Commit Count:', commitCount);
    console.log('Branch Count:', branchCount);
    console.log('Merger Request Count:', mergeRequestCount);
    console.log('AI Review Count:', aiReviewCount);

    projectData.languages.forEach((lang: Language) => {
      console.log(`Language: ${lang.name}, Percentage: ${lang.share}, Color: ${lang.color}`);
    });

    console.log('Programming Languages Data:', programmingLanguagesData);

    return {
      programmingLanguagesData,
      commitCount,
      branchCount,
      mergeRequestCount,
      aiReviewCount,
    };
  }

  return null;
};
